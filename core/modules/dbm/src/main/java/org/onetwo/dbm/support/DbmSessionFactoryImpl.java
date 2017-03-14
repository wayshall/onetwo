package org.onetwo.dbm.support;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.mapper.JdbcDaoRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.support.SimpleDbmInnerServiceRegistry.DbmServiceRegistryCreateContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DbmSessionFactoryImpl implements InitializingBean, DbmSessionFactory {

	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private PlatformTransactionManager transactionManager;
	private DataSource dataSource;
	private ApplicationContext applicationContext;
	

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;

	private RowMapperFactory rowMapperFactory;
	private SQLSymbolManager sqlSymbolManager;
	private SequenceNameManager sequenceNameManager;
	
	private DefaultDatabaseDialetManager databaseDialetManager;
	protected DbmConfig dataBaseConfig;
	private SimpleDbmInnerServiceRegistry serviceRegistry;
	protected String[] packagesToScan;
	
	private AtomicLong idGenerator = new AtomicLong(0);
	
	public DbmSessionFactoryImpl(ApplicationContext applicationContext, PlatformTransactionManager transactionManager,
			DataSource dataSource) {
		super();
		this.transactionManager = transactionManager;
		this.dataSource = dataSource;
	}

	@Override
	public void afterPropertiesSet() {
		if(transactionManager==null){
			Map<String, DataSourceTransactionManager> tms = SpringUtils.getBeansAsMap(applicationContext, DataSourceTransactionManager.class);
			Entry<String, DataSourceTransactionManager> tm = null;
			for(Entry<String, DataSourceTransactionManager> entry : tms.entrySet()){
				if(entry.getValue().getDataSource().equals(dataSource)){
					tm = entry;
					break;
				}
			}
			if(tm!=null){
				this.transactionManager = tm.getValue();
				if(logger.isDebugEnabled()){
					logger.debug("auto find DataSourceTransactionManager for current dataSource: {}", tm.getKey());
				}
			}else{
				throw new DbmException("no DataSourceTransactionManager configurate for dataSource: " + dataSource);
			}
		}
		if(serviceRegistry==null){
			DbmServiceRegistryCreateContext context = new DbmServiceRegistryCreateContext(applicationContext, dataSource);
			this.serviceRegistry = SimpleDbmInnerServiceRegistry.obtainServiceRegistry(context);
		}
		
		this.init();
	}
	
	protected void init() {
		/*this.serviceRegistry = new SimpleDbmInnserServiceRegistry();
		this.serviceRegistry.initialize(getDataSource(), packagesToScan);*/
		SimpleDbmInnerServiceRegistry serviceRegistry = getServiceRegistry();
		Assert.notNull(serviceRegistry);
		
		if(dataBaseConfig==null){
			this.dataBaseConfig = serviceRegistry.getDataBaseConfig();
		}
		this.databaseDialetManager = serviceRegistry.getDatabaseDialetManager();
		this.dialect = serviceRegistry.getDialect();
		this.mappedEntryManager = serviceRegistry.getMappedEntryManager();
		this.sqlSymbolManager = serviceRegistry.getSqlSymbolManager();
		this.rowMapperFactory = serviceRegistry.getRowMapperFactory();
		this.sequenceNameManager = serviceRegistry.getSequenceNameManager();
		
		if(ArrayUtils.isNotEmpty(packagesToScan)){
			mappedEntryManager.scanPackages(packagesToScan);
		}

		if(this.rowMapperFactory==null){
			this.rowMapperFactory = new JdbcDaoRowMapperFactory();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> RowMapper<T> getRowMapper(Class<T> type){
		return (RowMapper<T>)this.rowMapperFactory.createRowMapper(type);
	}

	public DbmSession getCurrentSession(){
		DbmSessionResourceHolder sessionHolder = (DbmSessionResourceHolder)TransactionSynchronizationManager.getResource(this);
		if(sessionHolder!=null && sessionHolder.isSynchronizedWithTransaction()){
//			sessionHolder.requested();
			return sessionHolder.getSession();
		}
		if(!TransactionSynchronizationManager.isSynchronizationActive()){
			throw new DbmException("no transaction synchronization in current thread!");
		}
		DbmSessionImpl session = new DbmSessionImpl(this, generateSessionId());
		session.setDebug(getDataBaseConfig().isLogSql());
		sessionHolder = new DbmSessionResourceHolder(session);
		TransactionSynchronizationManager.bindResource(this, sessionHolder);
		
		DbmTransactionSynchronization synchronization = new DbmTransactionSynchronization(sessionHolder);
		sessionHolder.setSynchronizedWithTransaction(true);
		TransactionSynchronizationManager.registerSynchronization(synchronization);
//		sessionHolder.requested();
		
		return session;
	}
	
	@Override
	public DbmSession openSession(){
		DbmSessionImpl session = new DbmSessionImpl(this, generateSessionId());
		session.setDebug(getDataBaseConfig().isLogSql());
		return session;
	}
	
	protected long generateSessionId(){
		return idGenerator.getAndIncrement();
	}

	@Override
	public SimpleDbmInnerServiceRegistry getServiceRegistry() {
		SimpleDbmInnerServiceRegistry registry = this.serviceRegistry;
		if(registry==null){
			this.serviceRegistry = SimpleDbmInnerServiceRegistry.obtainServiceRegistry(new DbmServiceRegistryCreateContext(dataSource));
		}
		return serviceRegistry;
	}

	public void setServiceRegistry(SimpleDbmInnerServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public DBDialect getDialect() {
		return dialect;
	}

	public void setDataBaseConfig(DbmConfig dataBaseConfig) {
		this.dataBaseConfig = dataBaseConfig;
	}

	@Override
	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	@Override
	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

	@Override
	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	@Override
	public DefaultDatabaseDialetManager getDatabaseDialetManager() {
		return databaseDialetManager;
	}

	@Override
	public DbmConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}
