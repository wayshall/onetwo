package org.onetwo.common.db.builder;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.InnerBaseEntityManager;

public class QueryBuilderFactory {

	public static QueryBuilder from(BaseEntityManager baseEntityManager, Class<?> entityClass){
		QueryBuilderImpl q = new QueryBuilderImpl(baseEntityManager.narrowAs(InnerBaseEntityManager.class), entityClass);
		return q;
	}
}
