package org.onetwo.dbm.support;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.QueryBuilder;

public interface DbmEntityManager extends BaseEntityManager {
	
	
//	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);
	
	public DbmDaoImplementor getDbmDao();
	
	public QueryBuilder createQueryBuilder(Class<?> entityClass);
	
}
