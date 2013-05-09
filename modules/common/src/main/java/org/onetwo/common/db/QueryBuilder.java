package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

public interface QueryBuilder {

	public String getAlias();
	
	public <T> T as(Class<T> queryBuilderClass);

	public Class<?> getEntityClass();

	public QueryBuilder debug();

	public QueryBuilder or(QueryBuilder subQuery);

	public QueryBuilder and(QueryBuilder subQuery);

	public QueryBuilder ignoreIfNull();

	public QueryBuilder throwIfNull();

	public QueryBuilder calmIfNull();

	public DefaultQueryBuilderField field(String... fields);

	public QueryBuilder select(String... fields);

	public QueryBuilder limit(int first, int size);

	public QueryBuilder asc(String... fields);

	public QueryBuilder desc(String... fields);

	public QueryBuilder distinct(String... fields);

	public QueryBuilder addField(QueryBuilderField field);
	
	public QueryBuilderJoin leftJoin(String table, String alias);

	public JFishQueryValue build();
	
	public <T> T one();
	
	public <T> List<T> list();
	
	public Map<Object, Object> getParams();
	

}