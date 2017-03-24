package org.onetwo.common.db.filequery.spi;

import javax.sql.DataSource;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;

public interface QueryProvideManager {

	public DbmQueryWrapper createSQLQuery(String sqlString, Class<?> entityClass);
//	public DbmQueryWrapper createQuery(String sqlString);
	public FileNamedQueryFactory getFileNamedQueryManager();
	
//	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry();
	
	public DataBase getDataBase();
	
	public DataSource getDataSource();
	
//	public DbmTypeMapping getSqlTypeMapping();

	public RowMapperFactory getRowMapperFactory();

	public DbmSessionFactory getSessionFactory();
//	public <T> T getRawManagerObject(Class<T> rawClass);
}
