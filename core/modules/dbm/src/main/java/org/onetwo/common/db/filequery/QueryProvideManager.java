package org.onetwo.common.db.filequery;

import javax.sql.DataSource;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;

public interface QueryProvideManager {

	public DbmQueryWrapper createSQLQuery(String sqlString, Class<?> entityClass);
//	public DbmQueryWrapper createQuery(String sqlString);
	public FileNamedQueryManager getFileNamedQueryManager();
	
	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry();
	
	public DataBase getDataBase();
	
	public DataSource getDataSource();
	
//	public DbmTypeMapping getSqlTypeMapping();

	public RowMapperFactory getRowMapperFactory();

	public <T> T getRawManagerObject();
	public <T> T getRawManagerObject(Class<T> rawClass);
}
