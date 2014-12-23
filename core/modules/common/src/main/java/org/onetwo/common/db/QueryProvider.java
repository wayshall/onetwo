package org.onetwo.common.db;

public interface QueryProvider {

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass);
	public DataQuery createQuery(String ejbqlString);
	public FileNamedQueryFactory<?> getFileNamedQueryFactory();
}
