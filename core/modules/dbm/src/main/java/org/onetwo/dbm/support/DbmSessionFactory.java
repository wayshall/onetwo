package org.onetwo.dbm.support;

import javax.sql.DataSource;

import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

public interface DbmSessionFactory {

	void afterPropertiesSet() throws Exception;

	<T> RowMapper<T> getRowMapper(Class<T> type);

	DbmSession getCurrentSession();
	DbmSession openSession();

	SimpleDbmInnerServiceRegistry getServiceRegistry();

	RowMapperFactory getRowMapperFactory();

	PlatformTransactionManager getTransactionManager();
	
	boolean isTransactionManagerEqualsCurrentTransactionManager();

	DataSource getDataSource();

	DBDialect getDialect();

	MappedEntryManager getMappedEntryManager();

	SQLSymbolManager getSqlSymbolManager();

	SequenceNameManager getSequenceNameManager();

	DefaultDatabaseDialetManager getDatabaseDialetManager();

	DbmConfig getDataBaseConfig();

}