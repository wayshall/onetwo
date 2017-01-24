package org.onetwo.common.db.sqlext;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.common.db.sqlext.ExtQuery.K.IfNull;

public interface ExtQuery {
	
	public static class Msg {
		public static final String THROW_IF_NULL_MSG = "the case value can not be null!";
	}
	
	final public static class K {
		public static enum IfNull {
			Calm,//not throw , not ignore
			Throw,
			Ignore
		}
		public static final Map<String, Object> ORDER_BY_MAP = new HashMap<String, Object>();
		public static final Map<String, String> JOIN_MAP = new LinkedHashMap<String, String>();

		public static final String DEBUG = "_EXTQUERY_DEBUG_NAME_KEY";
		public static final String IF_NULL = ":if-null";
//		public static final String SQL_QUERY = ":sql-query";

		public static final String NO_PREFIX = ".";//不会加 "ent."

		public static final String FUNC = "&";
//		public static final String RAW_FUNC = "#";// raw sql function
		public static final String PREFIX_REF = "@";//加上 "ent."

		public static final String QUERY_CONFIG = ":query_config";
		
		public static final String FIRST_RESULT = ":firstResult";
		public static final String MAX_RESULTS = ":maxResults";
		
		public static final String OR = ":or";
		public static final String AND = ":and";
//		public static final String RAW_QL = ":raw-ql";

		public static final String SELECT = ":select";
		public static final String UNSELECT = ":unselect";
		public static final String SQL_SELECT = ":sql-select";//value is RawSqlWrapper
		public static final String DISTINCT = ":distinct";
		public static final String COUNT = ":count";
//		public static final String CACHEABLE = ":cacheable";//是否缓存查询对象，避免重复解释，暂时没实现
		
		public static final String ASC = ":asc";
		public static final String DESC = ":desc";
		public static final String ORDERBY = ":orderBy";

		public static final String DATA_FILTER = ":dataFilter";
//		public static final String INCLUDE = ":include";
		
		public static final String SQL_JOIN = ":sql-join";//value is RawSqlWrapper
		public static final String JOIN_IN = ":join-in"; //hql的join in写法
		public static final String FETCH = ":fetch";//put(":fetch", "obj1") hql的left join fetch obj1写法 
		public static final String LEFT_JOIN_FETCH = ":left-join-fetch";// put(":left-join-fetch", "obj1") hql的left join fetch obj1写法 
		public static final String JOIN_FETCH = ":join-fetch";//i hql join fetch 写法
		public static final String JOIN = ":join"; // hql join写法
		public static final String LEFT_JOIN = ":left-join";//o
		
		/****
		 * 是否触发监听器
		 */
		public static final String LISTENERS = ":listeners";
		
		static{
			ORDER_BY_MAP.put(ASC, " asc");
			ORDER_BY_MAP.put(DESC, " desc");
			ORDER_BY_MAP.put(ORDERBY, "");

			JOIN_MAP.put(FETCH, "left join fetch");
			JOIN_MAP.put(LEFT_JOIN_FETCH, "left join fetch");
			JOIN_MAP.put(JOIN_FETCH, "join fetch");
			JOIN_MAP.put(JOIN_IN, "in");
			JOIN_MAP.put(JOIN, "join");
			JOIN_MAP.put(LEFT_JOIN, "left join");
			JOIN_MAP.put(SQL_JOIN, "");
		}
		
		public static Object getMappedValue(String key){
			return getMappedValue(key, null);
		}
		
		public static Object getMappedValue(String key, Object def){
			Object val = ORDER_BY_MAP.get(key);
			if(val==null)
				val = def;
			return val;
		}
		
		private K(){}
		
	}
	
	public void initQuery();
	
	public boolean hasBuilt();

	public ExtQuery build();

//	public Map getParams();

//	public void setParams(Map params);
	
	public Map<Object, Object> getParams();
	

	public ParamValues getParamsValue();
	
	public IfNull getIfNull();

//	public List getValues();

	public String getSql();

//	public StringBuilder getWhere();

	public Class<?> getEntityClass();
//	public boolean isSqlQuery();
	

}