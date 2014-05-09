package org.onetwo.common.db;

public interface CreateQueryable {

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass);
	public DataQuery createQuery(String ejbqlString);
	public FileNamedQueryFactory<?> getFileNamedQueryFactory();
}
