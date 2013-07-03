package org.onetwo.common.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.L;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ExtQueryImpl implements ExtQuery {
	protected final Logger logger = Logger.getLogger(ExtQueryImpl.class);


	public static final String[] SQL_KEY_WORKDS = new String[]{" ", ";", ",", "(", ")"};

	private Class entityClass;
	protected String alias;
	private Map params;
	private ParamValues paramsValue;
	// private Map<String, Object> paramsValue = new LinkedHashMap<String,
	// Object>();
	private SQLSymbolManager symbolManager;

	protected StringBuilder sql;
	protected StringBuilder select;
	protected StringBuilder join;
	protected StringBuilder where;
	protected StringBuilder orderBy;
	
	protected Integer firstResult = 0; 
	protected Integer maxResults = -1;
	
	protected boolean subQuery;

	private boolean debug;
//	private boolean throwIfCaseValueIsNull;
	private IfNull ifNull;
	
	private boolean sqlQuery = false;
	
	private boolean ignoreQuery;
	
	private boolean hasBuilt;
	
	private String countValue;
	
	private Map<String, Object> queryConfig;
	
	public ExtQueryImpl(Class<?> entityClass, String alias, Map params, SQLSymbolManager symbolManager) {
		this.entityClass = entityClass;
		if(StringUtils.isBlank(alias)){
			alias = StringUtils.uncapitalize(entityClass.getSimpleName());
		}
		this.alias = alias;
		this.params = params;
		this.symbolManager = symbolManager;
		
		this.init(entityClass, this.alias);
	}
	
	protected void init(Class<?> entityClass, String alias){
		this.debug = getValueAndRemoveKeyFromParams(K.DEBUG, debug);
		this.ifNull = getValueAndRemoveKeyFromParams(K.IF_NULL, IfNull.Ignore);
		setSqlQuery(getValueAndRemoveKeyFromParams(K.SQL_QUERY, sqlQuery));

		this.firstResult = getValueAndRemoveKeyFromParams(K.FIRST_RESULT, firstResult);
		this.maxResults = getValueAndRemoveKeyFromParams(K.MAX_RESULTS, maxResults);
		this.countValue = getValueAndRemoveKeyFromParams(K.COUNT, countValue);
		
		//query config
		Object qc = getValueAndRemoveKeyFromParams(K.QUERY_CONFIG, queryConfig);
		if(qc instanceof Object[]){
			this.queryConfig = CUtils.asMap((Object[])qc);
		}else{
			this.queryConfig = (Map)qc;
		}

		PlaceHolder holder = symbolManager.getPlaceHolder();
		if(isSqlQuery()){
			holder = PlaceHolder.POSITION;
		}
		this.paramsValue = new ParamValues(holder, symbolManager.getSqlDialet());
	}
	
	public SQLFunctionManager getSqlFunctionManager() {
		return DefaultSQLFunctionManager.get();
	}

	protected <T> T getValueAndRemoveKeyFromParams(String key, T def){
		if(!this.params.containsKey(key))
			return def;
		T value = (T)this.params.get(key);
		this.params.remove(key);
		return value==null?def:value;
	}
	
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
		this.alias = StringUtils.uncapitalize(entityClass.getSimpleName());
	}
	
	public boolean needSetRange(){
		return this.firstResult>=0 && this.maxResults!=-1;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public SQLSymbolManager getSymbolManager() {
		return symbolManager;
	}

	public ExtQuery build() {
		String fname = "build ext query";
		if(isDebug())
			UtilTimerStack.push(fname);
		this.buildSelect().buildJoin().buildOrderBy();
		sql = new StringBuilder();
		sql.append(select);
		if (join != null)
			sql.append(join);
		if (params.isEmpty()) {
			if (orderBy != null)
				sql.append(orderBy);
			return this;
		}
		
		this.buildWhere();
		if(where!=null)
			sql.append(where);
		if (orderBy != null)
			sql.append(orderBy);

		if (isDebug()) {
			if(logger.getLevel()!=null){
				logger.info("generated sql : " + sql);
				logger.info("params : " + (Map) this.paramsValue.getValues());
			}else{
				LangUtils.println("generated sql : " + sql);
				LangUtils.println("params : " + (Map) this.paramsValue.getValues());
			}
		}

		if(isDebug())
			UtilTimerStack.pop(fname);
		
		this.hasBuilt = true;
		return this;
	}

	protected ExtQueryImpl buildSelect() {
		select = new StringBuilder();
		
		if(getParams().containsKey(K.SQL_SELECT)){
			Object rawSqlObj = getParams().get(K.SQL_SELECT);
			if(rawSqlObj==null)
				return this;
			if(!(rawSqlObj instanceof RawSqlWrapper)){
				LangUtils.throwBaseException("it must be a sqlwrapper : " + rawSqlObj);
			}
			RawSqlWrapper wrapper = (RawSqlWrapper)rawSqlObj;
			if(!wrapper.isBlank()){
				select.append(wrapper.getRawSql());
			}
			params.remove(K.SQL_SELECT);
			return this;
		}

		Object selectValule = null;
		select.append("select ");
		if(hasParams(K.DISTINCT)){
			select.append("distinct ");
		}

		String selectKey = K.SELECT;
		selectValule = params.get(selectKey);
		if(selectValule==null){
			selectKey = K.DISTINCT;
			selectValule = params.get(selectKey);
		}
		if (selectValule != null) {
			params.remove(selectKey);
			Object[] selectList = null;
			if(selectValule instanceof String){
				selectList = StringUtils.split(selectValule.toString(), ",");
			}else if(selectValule.getClass().isArray()){
				selectList = (String[])selectValule;
			}else{
				selectList = (Object[])LangUtils.asList(selectValule).toArray();
			}
			for (int i = 0; i < selectList.length; i++) {
				if (i != 0)
					select.append(", ");

				select.append(getSelectFieldName(selectList[i].toString()));
				/*if(this.alias.equals(selectList[i]))
					select.append(selectList[i]);
				else
					select.append(getFieldName(selectList[i].toString()));*/
			}
			select.append(" ");
		} else if(StringUtils.isNotBlank(countValue)){
			select.append("count(").append(countValue).append(") ");
		} else {
			select.append(getDefaultSelectFields(entityClass, this.alias)).append(" ");
		}
		
		select.append("from ").append(getSelectFromName(entityClass)).append(" ").append(this.alias).append(" ");
		return this;
	}

	
	public String getSelectFieldName(String f) {
		if(this.alias.equals(f)){
			return f;
		}else{
			return appendAlias(translateAt(f));
		}
	}
	
	protected String getSelectFromName(Class<?> entityClass){
		return entityClass.getSimpleName();
	}
	
	protected String getDefaultSelectFields(Class<?> entityClass, String alias){
		return alias;
	}

	protected ExtQueryImpl buildJoin() {
		join = new StringBuilder();
		/*buildJoin(join, K.JOIN_FETCH, false);//inner
		buildJoin(join, K.FETCH, false);
		buildJoin(join, K.JOIN, false);
		buildJoin(join, K.LEFT_JOIN, false);//outer
		buildJoin(join, K.JOIN_IN, true);*/
		
		for(String key : K.JOIN_MAP.keySet()){
			if(K.JOIN_IN.equals(key)){
				buildJoin(join, key, true);
			}else if(K.SQL_JOIN.equals(key)){
				Object rawSqlObj = this.getParams().get(key);
				if(rawSqlObj==null)
					return this;
				if(!(rawSqlObj instanceof RawSqlWrapper))
					LangUtils.throwBaseException("it must a sql wrapper : " + rawSqlObj);
				RawSqlWrapper wrap = (RawSqlWrapper) rawSqlObj;
				if(!wrap.isBlank()){
					join.append(wrap.getRawSql()).append(" ");
				}
				getParams().remove(K.SQL_JOIN);
			}else{
				buildJoin(join, key, false);
			}
		}
		return this;
	}

	protected ExtQueryImpl buildJoin(StringBuilder joinBuf, String joinKey, boolean hasParentheses) {
		if (!hasParams(joinKey))
			return this;
		String joinWord = K.JOIN_MAP.get(joinKey);
		Object value = this.getParams().get(joinKey);
		List<String> fjoin = MyUtils.asList(value);
		if(fjoin==null)
			return this;
		
		// int index = 0;
		boolean hasComma = K.JOIN_IN.equals(joinKey);
		for (String j : fjoin) {
			String[] jstrs = StringUtils.split(j, ":");
			if(hasComma){
				joinBuf.append(", ");
			}
			if(jstrs.length>1)//alias
				joinBuf.append(joinWord).append(hasParentheses?"(":" ").append(getFieldName(jstrs[0])).append(hasParentheses?") ":" ").append(jstrs[1]).append(" ");
			else
				joinBuf.append(joinWord).append(hasParentheses?"(":" ").append(getFieldName(j)).append(hasParentheses?") ":" ");
		}
		this.getParams().remove(joinKey);
		return this;
	}
	
	public String translateAt(String f){
		if(f.indexOf(K.PREFIX_REF)!=-1){
			f = f.replace(K.PREFIX_REF, this.alias+".");
		}
		return f;
	}
	
	public String appendAlias(String f){
		String newf = f;
		if(f.startsWith(K.NO_PREFIX)){
			newf = f.substring(K.NO_PREFIX.length());
		}else{
			if(!f.startsWith(this.alias + "."))
				f = this.alias + "." + f;
			
			newf = f;
		}
		return newf;
	}
	
	protected void checkFieldNameValid(String field){
		Assert.hasText(field);
		for(String str : SQL_KEY_WORKDS){
			if(field.indexOf(str)!=-1)
				LangUtils.throwBaseException("the field is inValid : " + field);
		}
	}
	
	public String getFieldName(String f) {
		f = appendAlias(translateAt(f));
		checkFieldNameValid(f);
		return f;
	}
	
	
	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	protected boolean hasParams(String key) {
		return this.params != null && !this.params.isEmpty() && this.params.containsKey(key);
	}

	protected ExtQuery buildWhere() {
		String swhere = this.buildWhere(params, false);
		if (StringUtils.isBlank(swhere))
			return this;
		where = new StringBuilder(swhere);
		return this;
	}
	

	String buildWhere(Map params, boolean isSubQuery) {
		String fname = "buildWhere";
		if(isDebug())
			UtilTimerStack.push(fname);
		StringBuilder where = new StringBuilder("");
		if (params == null || params.isEmpty())
			return where.toString();

		// List paramsValue = new ArrayList();
		// StringBuilder causeScript;
		// causeScript = new StringBuilder();
		boolean first = true;
		String h = null;
		int index = 0;
		for (Map.Entry<Object, Object> entry : (Set<Map.Entry<Object, Object>>) params.entrySet()) {

			Object fields = entry.getKey();
			Object values = entry.getValue();

			if (K.ASC.equals(fields) || K.DESC.equals(fields) || MyUtils.isEmpty(fields))
				continue;
			
			/*if(ExtQueryUtils.isContinueByCauseValue(values, ifNull)){
				continue;
			}*/

			if (K.OR.equals(fields)) {
				if (!Map.class.isAssignableFrom(values.getClass()))
					throw new ServiceException("sub query's vaue must be map!");
				Map subParams = (Map) values;
				h = this.buildWhere(subParams, true);
				where.append("or ");
			} else if(K.AND.equals(fields)){
				if (!Map.class.isAssignableFrom(values.getClass()))
					throw new ServiceException("sub query's vaue must be map!");
				Map subParams = (Map) values;
				h = this.buildWhere(subParams, true);
				
				if(!first)
					where.append("and ");
			} else if(K.RAW_QL.equals(fields)){
				if (!values.getClass().isArray() && !List.class.isAssignableFrom(values.getClass()) && !String.class.isAssignableFrom(values.getClass()))
					throw new ServiceException("raw-ql args error: " + values);
				List listValue = L.tolist(values, true);
				h = (String)listValue.get(0);
				if(!h.endsWith(" "))
					h += " ";
				this.paramsValue.directAddValue(listValue.subList(1, listValue.size()));
				
				if(!first)
					where.append("and ");
			}else{
				h = buildFieldQueryString(fields, values);
				if (StringUtils.isBlank(h))
					continue;
				
				if(index>0)
					where.append("and ");
			}

			if (StringUtils.isBlank(h))
				continue;

			// causeScript.append(h);

			if (first && !isSubQuery) {
				where.append("where ");
				first = false;
			}
			where.append(h);
			index++;

		}
		if (isSubQuery) {
			where.insert(0, "( ");
			where.insert(where.length(), ") ");
		}
		if(isDebug())
			UtilTimerStack.pop(fname);
		return where.toString();
	}

	protected String buildFieldQueryString(Object fields, Object values) {
		List fieldList =  MyUtils.asList(fields);
		List valueList = ExtQueryUtils.processValue(fields, values, ifNull);
		
		//ignore null
		if (valueList == null || valueList.isEmpty())
			return null;

		int index = 0;
		String h = null;
		StringBuilder causeScript = new StringBuilder();
 
		for (int i = 0; i < fieldList.size(); i++) {
			Object p = fieldList.get(i);

//			List valueList = (List<String>) MyUtils.asList(values);//asList(values, false);
			
			/*if (valueList == null || valueList.isEmpty())
				return null;*/

			/*if(ExtQueryUtils.isContinueByCauseValue(values, ifNull)){
				continue;
			}*/

			Object v = null;
			try {
				if (fieldList.size() == 1){
					if(valueList.size()==1)
						v = valueList.get(0);
					else
						v = values;
				}else{
					v = valueList.get(i);
				}
			} catch (IndexOutOfBoundsException e) {
				v = valueList.get(0);//if can't find the corresponding value, get the first one.
			}

			/*if(v==null)
				return null;*/
			
			/*if(ExtQueryUtils.isContinueByCauseValue(values, ifNull)){
				continue;
			}*/

//			String[] sp = StringUtils.split(p.toString(), SQLSymbolManager.SPLIT_SYMBOL);
			QueryField qf = null;
			if(p instanceof String){
				qf = new QueryFieldImpl(p.toString());
			}else if(p instanceof QueryField){
				qf = (QueryField) p;
			}else{
				LangUtils.throwBaseException("error field expression : " + p);
			}
			qf.init(this, v);
			
//			SQLSymbolParserContext context = SQLSymbolParserContext.create(qf.getFieldName(), v, paramsValue, ifNull);
			h = getSymbolManager().getHqlSymbolParser(qf.getOperator()).parse(qf);

			if (StringUtils.isBlank(h))
				continue;

			if (index > 0)
				causeScript.append("or ");

			causeScript.append(h);
			index++;
		}

		if (index > 1) {
			causeScript.insert(0, "( ");
			causeScript.insert(causeScript.length() - 1, " )");
		}

		return causeScript.toString();
	}

	protected ExtQuery buildOrderBy() {
		orderBy = new StringBuilder();
		/*boolean hasAsc = buildOrderby0(K.ASC);
		boolean hasDes = buildOrderby0(K.DESC);*/
		
		boolean hasOrderBy = false;
		List<String> orderbys = new ArrayList<String>(3);
		for(Map.Entry entry : (Set<Map.Entry>)this.params.entrySet()){
			if(K.ORDER_BY_MAP.containsKey(entry.getKey())){
				orderbys.add((String)entry.getKey());
			}
		}
		for(String order : orderbys){
			if(buildOrderby0(order))
				hasOrderBy = true;
		}
		
		if (!hasOrderBy) {
			this.buildDefaultOrderBy();
		}
		 
		return this;
	}
	
	protected void buildDefaultOrderBy(){
		if (!subQuery) {
			String sortField = getDefaultOrderByFieldName();
			if(StringUtils.isNotBlank(sortField))
				orderBy.append("order by ").append(getFieldName(sortField)).append(" desc "); 
		}
	}
	
	protected String getDefaultOrderByFieldName(){
		return "id";
	}
	
	protected boolean buildOrderby0(String order){
		boolean hasOrderBy = false;
		if(!params.keySet().contains(order))
			return false;
		
		Object ascValue = params.remove(order);
		if(ascValue==null)
			return false;
		
		Object orderValue = K.getMappedValue(order);
		Object[] orderList = null;
		if(LangUtils.isMultiple(ascValue))
			orderList = (Object[]) LangUtils.asList(ascValue).toArray();
		else
			orderList = StringUtils.split(ascValue.toString(), ",");
		String orderField = null;

		if(orderBy==null || orderBy.length()<1){
			orderBy.append("order by ");
		}else{
			orderBy.append(", ");
		}
		for (int i = 0; i < orderList.length; i++) {
			orderField = orderList[i].toString().trim();
			if (i == 0) {
				hasOrderBy = true;
			} else{
				orderBy.append(", ");
			}
			int oIndex = orderField.indexOf(':');
			if(oIndex==-1){
				if(K.ORDERBY.equals(order))
					orderBy.append(orderField).append(orderValue);
				else
					orderBy.append(getFieldName(orderField)).append(orderValue);
			}else{
				String f = orderField.substring(0, oIndex);
				String nullsOrder = orderField.substring(oIndex+1);
				nullsOrder = symbolManager.getSqlDialet().getNullsOrderby(nullsOrder);
				orderBy.append(getFieldName(f)).append(" ").append(nullsOrder).append(" ").append(orderValue);
			}
		}
		return hasOrderBy;
	}

	/*
	 * public Map<String, Object> getParamsValue() { return paramsValue; }
	 */

	public ParamValues getParamsValue() {
		return paramsValue;
	}

	/*
	 * public List getValues() { return new
	 * ArrayList(this.paramsValue.values()); }
	 */

	public String getSql() {
		return sql.toString();
	}

	public StringBuilder getSelect() {
		return select;
	}

	public StringBuilder getWhere() {
		return where;
	}

	public StringBuilder getOrderBy() {
		return orderBy;
	}
	

	protected String buildCountSql(String sql){
		String hql = sql;
		String countField = getDefaultField("id");

		if(hql.indexOf("{")!=-1 || hql.indexOf("}")!=-1)
			hql = hql.replace("{", "").replace("}", "");
		
		if(hql.indexOf(" group by ")!=-1){
//			int index = countField.lastIndexOf('.');
//			countField = countField.substring(index+1);
			if(StringUtils.isNotBlank(countValue)){
				countField = countValue;
			}else{
				countField = "count_entity." + countField;
			}
			hql = "select count("+countField+") from (" + hql + ") count_entity ";
		}else{
			hql = StringUtils.substringAfter(hql, "from ");
			hql = StringUtils.substringBefore(hql, " order by ");

			if(StringUtils.isNotBlank(countValue))
				countField = countValue;
			
			hql = "select count(" + countField + ") from " + hql;
			
			/*if(StringUtils.isNotBlank(countValue))
				countField = countValue;
			hql = ExtQueryUtils.buildCountSql(hql, countField);*/
		}
		return hql;
	}
	
	protected String getDefaultField(String name){
		String countName = getFieldName(name);
		if(StringUtils.isBlank(countName)){
			countName = "*";
		}
		return countName;
	}

	public String getCountSql() {
//		String countSql = MyUtils.getCountSql(sql.toString(), getFieldName("id"));
		String countSql = buildCountSql(sql.toString());
		if (isDebug()) {
			logger.info("generated count sql : " + countSql);
			logger.info("params : " + (Map) this.paramsValue.getValues());
		}
		return countSql;
	}

	public boolean isSubQuery() {
		return subQuery;
	}

	public void setSubQuery(boolean subQuery) {
		this.subQuery = subQuery;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public IfNull getIfNull() {
		return ifNull;
	}

	public void setIfNull(IfNull ifNull) {
		this.ifNull = ifNull;
	}
	
	public boolean calmIfNull(){
		return IfNull.Calm == this.ifNull;
	}
	
	public boolean isIgnoreQuery(){
		return ignoreQuery;
	}
	
	public boolean isSqlQuery() {
		return sqlQuery;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public static void main(String[] args) {

		Map<Object, Object> properties = new LinkedHashMap<Object, Object>();

		properties.put("&LOWER(name)", "way");
		properties.put("&substring(name, 5, 1)", "w");

		ExtQuery q = SQLSymbolManagerFactory.getInstance().getJPA().createQuery(Object.class, "mag", properties);
		q.build();
		
	}

	public boolean hasBuilt() {
		return hasBuilt;
	}

	public Map<String, Object> getQueryConfig() {
		if(queryConfig==null){
			return Collections.EMPTY_MAP;
		}
		return queryConfig;
	}

	protected void setSqlQuery(boolean sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
}
