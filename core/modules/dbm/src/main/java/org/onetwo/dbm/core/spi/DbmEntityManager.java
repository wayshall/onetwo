package org.onetwo.dbm.core.spi;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.QueryBuilder;

public interface DbmEntityManager extends BaseEntityManager {
	
	
//	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);
	
	public DbmSessionImplementor getCurrentSession();
	
	public QueryBuilder createQueryBuilder(Class<?> entityClass);
	
}
