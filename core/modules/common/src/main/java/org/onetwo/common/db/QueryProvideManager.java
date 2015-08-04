package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.NamespaceProperty;

public interface QueryProvideManager {

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass);
	public DataQuery createQuery(String ejbqlString);
	public <T extends NamespaceProperty> FileNamedQueryFactory<T> getFileNamedQueryFactory();
}
