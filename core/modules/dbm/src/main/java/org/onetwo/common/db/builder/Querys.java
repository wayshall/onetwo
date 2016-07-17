package org.onetwo.common.db.builder;

import org.onetwo.common.db.BaseEntityManager;

/****
 * alias for QueryBuilderFactory 
 * @author way
 *
 */
public class Querys {

	public static QueryBuilder from(BaseEntityManager baseEntityManager, Class<?> entityClass){
		return QueryBuilderFactory.from(baseEntityManager, entityClass);
	}
}
