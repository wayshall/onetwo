package org.onetwo.common.db.builder;

/***
 * 用于创建没有baseEntityManager的QueryBuilder，这种QueryBuilder可作为参数传给baseEntityManager查询，用于在多个baseEntityManager共享……
 * @author way
 *
 */
final public class QueryBuilders {

	public static QueryBuilder create(Class<?> entityClass){
		QueryBuilderImpl q = new QueryBuilderImpl(null, entityClass);
		return q;
	}
	
	private QueryBuilders(){
	}
}
