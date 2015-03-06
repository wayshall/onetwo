package org.onetwo.common.db;

import org.onetwo.common.db.QueryBuilderImpl.SubQueryBuilder;

final public class QueryBuilderCreator {

	public static QueryBuilder from(Class<?> entityClass){
		QueryBuilder q = new QueryBuilderImpl(entityClass);
		return q;
	}

	public static SubQueryBuilder sub(){
		SubQueryBuilder q = new SubQueryBuilder();
		return q;
	}
	
	private QueryBuilderCreator(){
	}
	
}
