package org.onetwo.common.hibernate;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.QueryProvider;

public interface HibernateQueryProvider extends QueryProvider {
	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass, boolean stateless);
	public DataQuery createQuery(String ejbqlString, boolean statful);
}
