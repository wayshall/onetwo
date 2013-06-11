package org.onetwo.common.db.sql;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.parser.SqlKeywords.SqlType;

/***
 * 对sql查询语句的封装
 * 可解释含有命名参数的sql语句
 * 可忽略值为null的条件
 * @author weishao
 *
 */
@SuppressWarnings("rawtypes")
public interface DynamicQuery extends QueryOrderByable {
	public boolean hasCompiled();
	public void compile();

	
//	public List<EventListener> getEventListers(SqlOperation operation);

//	public List getConditionValues();
//	public List<Condition> getConditions();
//	public List<Condition> getActualConditions();
	
	public boolean hasConditions();

	public DynamicQuery setParameter(int index, Object value);
	
	public DynamicQuery setParameters(List<?> args);

	public DynamicQuery setParameter(String varname, Object value);

	public DynamicQuery setParameters(Map<String, Object> params);
	
	public DynamicQuery setParameters(Object entity);
	
	public int getParameterCount();

	public int getActualParameterCount();

	public List getValues();
	
	public DynamicQuery ignoreIfNull();

	public String getTransitionSql();

//	public String getTransitionPageSql();

//	public String getTransitionCountPageSql();
	public String getCountSql();

	public void setCountSql(String countSql);

//	public List getPageValues() ;
	
	public DynamicQuery setFirstRecord(int firstRecord);
	
	public DynamicQuery setMaxRecord(int maxRecord);
	
//	public AnotherQuery asc(String... fields);
	
//	public AnotherQuery desc(String... fields);
	
	public int getFirstRecord();
	public int getMaxRecords();

//	public Map<String, Object> getParams();
//	public boolean isBatch();
	
	public Class<?> getEntityClass();
	
	public boolean isPage();
	
	public void setEntityClass(Class<?> entityClass);
	
	public SqlType getSqlType();
	
//	public QueryContext getContext();
}