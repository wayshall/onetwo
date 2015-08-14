package org.onetwo.common.db.filequery;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DataQuery;

public interface QueryProvideManager {

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass);
	public DataQuery createQuery(String ejbqlString);
	public FileNamedQueryFactory<? extends NamespaceProperty> getFileNamedQueryFactory();
	
	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry();
	
	public DataBase getDataBase();
}
