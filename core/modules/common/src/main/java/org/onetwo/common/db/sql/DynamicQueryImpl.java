package org.onetwo.common.db.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.parser.SqlKeywords.SqlType;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.SToken;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

/*******************************************************************************
 * AnotherQuery的实现
 * 
 * @author weishao
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicQueryImpl implements DynamicQuery {

	private static final String REPLACE_SQL = "1=1";

	protected Logger logger = MyLoggerFactory.getLogger(this.getClass());
	protected Class<?> entityClass;

	protected String originalSql;
	protected String transitionSql;
	protected String countSql;

	protected List<Condition> conditions = new ArrayList<Condition>();
	protected List values = new ArrayList();

	protected int firstRecord = -1;
	protected int maxRecords;
	
	protected boolean hasCompiled;
	protected IfNull ifNull = K.IfNull.Throw;
	
	protected List<SToken> tokens;
	

	protected DynamicQueryImpl(String sql) {
		this(sql, null);
	}
	
	protected DynamicQueryImpl(String sql, Class<?> entityClass) {
		this.entityClass = entityClass;
		SqlCauseParser sqlParser = SqlCauseParser.SIMPLE;
		tokens = sqlParser.parseSql(sql);
		originalSql = sql;
		this.parseQuery();
	}
	
	protected DynamicQueryImpl(String sql, List<SToken> tokens, Class<?> entityClass) {
		originalSql = sql;
		this.entityClass = entityClass;
		this.tokens = tokens;
		this.parseQuery();
	}
	
	protected void parseQuery() {
		int index = 0;
		for(SToken stoken : this.tokens){
			if(ConditionToken.class.isInstance(stoken)){
				Condition cond = new Condition((ConditionToken)stoken, index++);
				this.conditions.add(cond);
			}
		}
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
		for(Condition cond : this.conditions){
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
		for(Condition cond : this.conditions){
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
		if(this.conditions.isEmpty()){
			this.transitionSql = this.originalSql;
			return ;
		}
		
		List<String> newStrs = new ArrayList<String>();
		int count = 0;
		SToken token;
		String tokenStr = null;
		for (int i = 0; i < this.tokens.size(); i++) {
			token = tokens.get(i);
			
			if (token instanceof ConditionToken) {
				Condition cond = null;
				cond = this.conditions.get(count++);
				
				if(IfNull.Ignore==this.ifNull){
					if(cond.isAvailable())
						tokenStr = cond.toSqlString();
					else
						tokenStr = REPLACE_SQL;
				}else{
					if(cond.isAvailable())
						tokenStr = cond.toSqlString();
					else
						LangUtils.throwBaseException("the parameter["+cond.getName()+"] has not value");
				}
			}else{
				tokenStr = token.getName();
			}
			newStrs.add(tokenStr);
		}
		
		this.transitionSql = StringUtils.join(newStrs, "");
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
		for (Condition cond : this.conditions) {
			if (!varname.equals(cond.getVarname()))
				continue;
			cond.setValue(value);
			setNotompiled();
			break;
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
	
	public List<Condition> getActualConditions(){
		if(this.ifNull!=IfNull.Ignore)
			return this.conditions;
		
		List<Condition> actaulConditions = new ArrayList<Condition>();
		for(Condition cond : this.conditions){
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
		if(StringUtils.isBlank(countSql)){
			this.countSql = buildCountSql(this.transitionSql, "");
		}
		return countSql;
	}
	

	public static String buildCountSql(String sql, String countValue){
		String countField = "*";
		String hql = StringUtils.substringAfter(sql, "from ");
//		hql = StringUtils.substringBefore(hql, " order by ");
		if(StringUtils.isBlank(hql)){
			hql = StringUtils.substringAfter(sql, "FROM ");
		}

		if(StringUtils.isNotBlank(countValue))
			countField = countValue;
		
		hql = "select count(" + countField + ") from " + hql;
		return hql;
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
		for(Condition cond : this.getActualConditions()){
			if(cond.isMutiValue()){
				values.addAll(cond.getValueAsList());
			}else{
				values.add(cond.getValue());
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

	@Override
	public void asc(String... fields) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void desc(String... fields) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SqlType getSqlType() {
		throw new UnsupportedOperationException();
	}

//	@Override
//	public QueryContext getContext() {
//		throw new UnsupportedOperationException();
//	}

}
