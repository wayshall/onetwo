package org.onetwo.dbm.core.spi;

import org.onetwo.common.db.filequery.spi.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.dbm.core.internal.DbmInterceptorManager;
import org.onetwo.dbm.core.internal.SimpleDbmInnerServiceRegistry.DbmServiceRegistryCreateContext;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.JdbcStatementParameterSetter;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.EntityValidator;
import org.onetwo.dbm.mapping.MappedEntryManager;

public interface DbmInnerServiceRegistry {

	void initialize(DbmServiceRegistryCreateContext context);

	DBDialect getDialect();

	DbmJdbcOperations getDbmJdbcOperations();

	JdbcStatementParameterSetter getJdbcParameterSetter();

	DbmInterceptorManager getInterceptorManager();

	MappedEntryManager getMappedEntryManager();

	SQLSymbolManager getSqlSymbolManager();

	SequenceNameManager getSequenceNameManager();

	DefaultDatabaseDialetManager getDatabaseDialetManager();

	DbmConfig getDataBaseConfig();

	RowMapperFactory getRowMapperFactory();

	<T> T getService(Class<T> clazz);

	<T> T getService(String name);

	<T> DbmInnerServiceRegistry register(T service);

	<T> DbmInnerServiceRegistry register(String name, T service);

	EntityValidator getEntityValidator();

	DbmTypeMapping getTypeMapping();

	SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry();

}