package org.onetwo.common.db.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.parser.SqlKeywords.SqlType;
import org.onetwo.common.db.parser.interceptors.DefaultDynamicQueryInterceptor;
import org.onetwo.common.db.parser.interceptors.DynamicQueryInterceptor;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sqlext.ExtQuery.K;
import org.onetwo.common.db.sqlext.ExtQuery.K.IfNull;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

/*******************************************************************************
 * AnotherQuery的实现
 * 
 * @author weishao
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JFishDynamicQueryImpl implements DynamicQuery {

//	private static final String REPLACE_SQL = "1=1 ";
	private static final DynamicQueryInterceptor DEFAULT_INTERCEPTOR = new DefaultDynamicQueryInterceptor();

	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected Class<?> entityClass;

	protected String originalSql;
	protected String transitionSql;
	protected String countSql;
	protected boolean hasBuildCountSql;

	protected List<SqlCondition> conditions = LangUtils.newArrayList();
	protected List values = new ArrayList();

	protected int firstRecord = -1;
	protected int maxRecords;
	
	protected boolean hasCompiled;
	protected IfNull ifNull = K.IfNull.Throw;
	
	protected SqlStatment statments;
	
	private DynamicQueryInterceptor interceptor;
	
	private QueryContext context = new QueryContext(this);
	
	private SqlType sqlType;

	public JFishDynamicQueryImpl(String sql) {
		this(sql, null);
	}
	
	public JFishDynamicQueryImpl(String sql, Class<?> entityClass) {
		this.entityClass = entityClass;
		JFishSqlParser sqlParser = new JFishSqlParser(sql);
		statments = sqlParser.parse();
		originalSql = sql;
		this.interceptor = DEFAULT_INTERCEPTOR;
		this.parseQuery();
	}

	public JFishDynamicQueryImpl(String sql, SqlStatment statments, Class<?> entityClass) {
		this(sql, statments, entityClass, null);
	}
	
	public JFishDynamicQueryImpl(String sql, SqlStatment statments, Class<?> entityClass, DynamicQueryInterceptor interceptors) {
		this.originalSql = sql;
		this.entityClass = entityClass;
		this.statments = statments;
		if(interceptors!=null)
			this.interceptor = interceptors;
		else
			this.interceptor = DEFAULT_INTERCEPTOR;
		this.parseQuery();
	}
	
	private void parseQuery() {
		this.sqlType = this.statments.getSqlType();
//		int index = 0;
		for(SqlObject sqlObj : this.statments.getSqlObjects()){
			//onParse
			this.interceptor.onParse(sqlObj, conditions);
//			if(SqlVarObject.class.isInstance(sqlObj)){
//				JFishConditon cond = new JFishConditon((SqlVarObject)sqlObj, index++);
//				this.conditions.add(cond);
//			}
		}
//		System.out.println("conditions: " + conditions);
	}

	public DynamicQuery ignoreIfNull(){
		this.ifNull = K.IfNull.Ignore;
		return this;
	}
	
	@Override
	public DynamicQuery setParameters(List<?> args) {
		Assert.notEmpty(args);
		
		if(!hasConditions())
			return this;
		int index = 0;
		for(Object val : args){
			if(index>=this.conditions.size()){
				LangUtils.throwBaseException("I have "+this.conditions.size()+" parameters, you give me "+args.size()+" , where I get so many parameters to set for you?");
			}
			this.conditions.get(index).setValue(val);
			index++;
		}
		setNotompiled();
		return this;
	}
	
	@Override
	public DynamicQuery setParameters(Map<String, Object> parameters) {
		if(LangUtils.isEmpty(parameters))
			return this;
		
		if(!hasConditions())
			return this;
		
		Object value;
		for(SqlCondition cond : this.conditions){
			value = parameters.get(cond.getVarname());
			cond.setValue(value);
		}
		setNotompiled();
		return this;
	}
	
	public boolean hasConditions(){
		return !this.conditions.isEmpty();
	}

	public DynamicQuery setParameters(Object bean) {
		if(!hasConditions())
			return this;
		for(SqlCondition cond : this.conditions){
			cond.setValue(ReflectUtils.getExpr(bean, cond.getVarname()));
		}
		setNotompiled();
		return this;
	}
	
	public boolean hasCompiled(){
		return hasCompiled;
	}
	
	public void setNotompiled(){
		hasCompiled = false;
	}

	public void compile() {
		if(hasCompiled){
			return ;
		}
		hasCompiled = true;
		if(this.conditions.isEmpty() && this.context.isEmpty()){
			this.transitionSql = this.originalSql;
			return ;
		}
		
		StringBuilder sql = new StringBuilder();
		int count = 0;
//		StringBuilder segment = new StringBuilder();;
		SqlCondition cond = null;
		for(SqlObject sqlObj : statments.getSqlObjects()){
//			segment.delete(0, segment.length());
			
			if (SqlVarObject.class.isInstance(sqlObj)) {
				cond = this.conditions.get(count++);
			}else{
				cond = null;
			}

			this.interceptor.onCompile(sqlObj, cond, ifNull, sql, context);
//			sql.append(segment);//.append(" ");
		}
		
		//translate
		this.transitionSql = this.interceptor.translateSql(sql.toString().trim(), context);
//		this.transitionSql = sql.toString().trim();
//		this.buildOrderBy();
		if (logger.isDebugEnabled())
			logger.info("=======>>> transitionSql: \n " + transitionSql);
	}
	

	public DynamicQuery setParameter(int index, Object value) {
		if(index>=this.conditions.size()){
			throw new BaseException("["+this.conditions+"] only has "+this.conditions.size()+" parameters. can find more paramemter index:"+index+", ");
		}
		this.conditions.get(index).setValue(value);
		setNotompiled();
		return this;
	}

	public DynamicQuery setParameter(String varname, Object value) {
		Assert.hasText(varname);
		Assert.notEmpty(this.conditions);
		for (SqlCondition cond : this.conditions) {
			if (!varname.equals(cond.getVarname()))
				continue;
			cond.setValue(value);
			setNotompiled();
//			break;
		}
		return this;
	}

	public int getParameterCount() {
		return this.conditions.size();
	}

	public int getActualParameterCount() {
		return this.getValues().size();
	}

	public List getPageValues() {
		List pageValue = this.getConditionValues();
		pageValue.add(firstRecord);
		pageValue.add(maxRecords);
		return pageValue;
	}

	public List getValues() {
		if(values==null || values.isEmpty()){
			values = this.getConditionValues();
		}
		return values;
	}
	
	public List<SqlCondition> getActualConditions(){
		if(this.ifNull!=IfNull.Ignore)
			return this.conditions;
		
		List<SqlCondition> actaulConditions = LangUtils.newArrayList();
		for(SqlCondition cond : this.conditions){
			if(cond.isIgnore())
				continue;
			actaulConditions.add(cond);
		}
		return actaulConditions;
	}
	public String getTransitionSql() {
		return transitionSql;
	}


	@Override
	public String getCountSql() {
		if(hasBuildCountSql){
			return countSql;
		}
		this.buildCountSql();
		return countSql;
	}
	
	protected void buildCountSql(){
		this.countSql = this.interceptor.translateCountSql(transitionSql, context);
		hasBuildCountSql = true;
	}
	
	

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}


	public DynamicQuery setFirstRecord(int firstRecord) {
		this.firstRecord = firstRecord;
		return this;
	}

	public DynamicQuery setMaxRecord(int maxRecord) {
		this.maxRecords = maxRecord;
		return this;
	}

	public boolean isPage() {
		return this.firstRecord > -1 && this.maxRecords > 0;
	}

	/***************************************************************************
	 * 查询条件设置
	 * 
	 * @param name
	 *            字段名称
	 * @param value
	 *            查询值
	 * @return
	 */
	public List getConditionValues(){
//		Assert.notEmpty(this.conditions);
		
		List values = new ArrayList();
		for(SqlCondition cond : this.getActualConditions()){
			if(cond.isMutiValue()){
				values.addAll(cond.getActualValueAsList());
			}else{
				values.add(cond.getActualValue());
			}
		}
		return values;
	}


	public int getFirstRecord() {
		return firstRecord;
	}


	public int getMaxRecords() {
		return maxRecords;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public QueryContext getContext() {
		return context;
	}

	@Override
	public void asc(String... fields) {
		this.context.asc(fields);
	}

	@Override
	public void desc(String... fields) {
		this.context.desc(fields);
	}

	public boolean isQuerySql(){
		return sqlType!=null && sqlType==SqlType.SELECT;
	}

	public SqlType getSqlType() {
		return sqlType;
	}
	
}
