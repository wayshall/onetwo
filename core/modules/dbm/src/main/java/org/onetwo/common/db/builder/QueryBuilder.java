package org.onetwo.common.db.builder;

import java.util.Map;

/***
 * @author way
 *
 */
public interface QueryBuilder {

	public String getAlias();
	
	public <T> T as(Class<T> queryBuilderClass);

	public Class<?> getEntityClass();
	
	public WhereCauseBuilder where();

	/*public QueryBuilder debug();

	public QueryBuilder or(QueryBuilder subQuery);

	public QueryBuilder and(QueryBuilder subQuery);

	public QueryBuilder addField(QueryBuilderField field);

	public QueryBuilder ignoreIfNull();

	public QueryBuilder throwIfNull();

	public QueryBuilder calmIfNull();

	public DefaultQueryBuilderField field(String... fields);*/

	public QueryBuilder select(String... fields);

	public QueryBuilder limit(int first, int size);

	public QueryBuilder asc(String... fields);

	public QueryBuilder desc(String... fields);

	public QueryBuilder distinct(String... fields);
	
	public QueryBuilderJoin leftJoin(String table, String alias);

//	public QueryBuilder build();
	public QueryAction toQuery();
	
	public Map<Object, Object> getParams();
	
//	public ParamValues getParamValues();
//	public String getSql();
	
	
//	public int execute();
}