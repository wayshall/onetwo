package org.onetwo.common.jfishdbm.support;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.jfishdbm.query.JFishQueryBuilder;

public interface DbmEntityManager extends BaseEntityManager {
	
	
//	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);
	
	public int removeAll(Class<?> entityClass);
	
	public DbmDaoImplementor getDbmDao();
	
	public JFishQueryBuilder createQueryBuilder(Class<?> entityClass);
	
}
