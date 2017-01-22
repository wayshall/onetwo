package org.onetwo.dbm.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.apache.commons.collections4.CollectionUtils;
import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DbmetaFetcher;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.dialet.MySQLDialect;
import org.onetwo.dbm.dialet.OracleDialect;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.jdbc.JdbcStatementParameterSetter;
import org.onetwo.dbm.jdbc.SpringJdbcResultSetGetter;
import org.onetwo.dbm.jdbc.SpringStatementParameterSetter;
import org.onetwo.dbm.jdbc.mapper.JFishRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.jpa.JFishSequenceNameManager;
import org.onetwo.dbm.jpa.JPAMappedEntryBuilder;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.mapping.EntityValidator;
import org.onetwo.dbm.mapping.JFishMappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MutilMappedEntryManager;
import org.onetwo.dbm.query.JFishSQLSymbolManagerImpl;
import org.onetwo.dbm.richmodel.MultiMappedEntryListener;
import org.onetwo.dbm.richmodel.RichModelCheckMappedEntryManagerListener;
import org.onetwo.dbm.richmodel.RichModelMappedEntryListener;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class SimpleDbmInnerServiceRegistry {

	public static SimpleDbmInnerServiceRegistry createServiceRegistry(DataSource dataSource, Validator validator){
		return createServiceRegistry(dataSource, validator, null);
	}
	public static SimpleDbmInnerServiceRegistry createServiceRegistry(DataSource dataSource, Validator validator, Collection<String> packagesToScan){
		SimpleDbmInnerServiceRegistry serviceRegistry = new SimpleDbmInnerServiceRegistry();
		serviceRegistry.initialize(dataSource, packagesToScan);
		if(validator!=null){
			serviceRegistry.setEntityValidator(new Jsr303EntityValidator(validator));
		}
		return serviceRegistry;
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	final private Map<String, Object> services = Maps.newConcurrentMap();

	private DBDialect dialect;
	private MappedEntryManager mappedEntryManager;
	private SQLSymbolManager sqlSymbolManager;
	private SequenceNameManager sequenceNameManager;
	private DefaultDatabaseDialetManager databaseDialetManager;
	private DbmConfig dataBaseConfig;
	private RowMapperFactory rowMapperFactory;
	private EntityValidator entityValidator;
	private JdbcStatementParameterSetter jdbcParameterSetter;
	private JdbcResultSetGetter jdbcResultSetGetter;
	private DbmTypeMapping typeMapping;
	
	public void initialize(DataSource dataSource, Collection<String> packagesToScan){
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDbmConfig();
		}
		if(jdbcParameterSetter==null){
			this.jdbcParameterSetter = new SpringStatementParameterSetter();
		}
		if(jdbcResultSetGetter==null){
			this.jdbcResultSetGetter = new SpringJdbcResultSetGetter();
		}
		if(typeMapping==null){
			this.typeMapping = new DbmTypeMapping();
		}
		if(databaseDialetManager==null){
			this.databaseDialetManager = new DefaultDatabaseDialetManager();
			this.databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
			this.databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		}
		
		
//		super.initDao();
		if(this.dialect==null){
			DBMeta dbmeta = DbmetaFetcher.create(dataSource).getDBMeta();
//			this.dialect = JFishSpringUtils.getMatchDBDiaclet(applicationContext, dbmeta);
			this.dialect = this.databaseDialetManager.getRegistered(dbmeta.getDbName());
			if (this.dialect == null) {
				throw new IllegalArgumentException("'dialect' is required");
			}
//			LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
			this.dialect.getDbmeta().setVersion(dbmeta.getVersion());
			this.dialect.initialize();
		}
		
		//mappedEntryManager
		if(mappedEntryManager==null){
			MutilMappedEntryManager _mappedEntryManager = new MutilMappedEntryManager();
//			this.mappedEntryManager.initialize();
			
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new JFishMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
//			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
			_mappedEntryManager.setMappedEntryBuilder(ImmutableList.copyOf(builders));
			this.mappedEntryManager = _mappedEntryManager;
			
		}
		if(CollectionUtils.isNotEmpty(packagesToScan)){
			MultiMappedEntryListener ml = new MultiMappedEntryListener();
			if(ClassUtils.isPresent("javassist.ClassPool", null)){
				ml.addListener(new RichModelMappedEntryListener());
			}else{
				ml.addListener(new RichModelCheckMappedEntryManagerListener());
//				logger.error("you must be add javassist to classpath if you want to use richmodel support!");
			}
			mappedEntryManager.setMappedEntryManagerListener(ml);
			mappedEntryManager.scanPackages(packagesToScan.toArray(new String[0]));
			if(logger.isInfoEnabled()){
				logger.info("scan model package: {}", packagesToScan);
			}
		}
		
		//init sql symbol
		if(sqlSymbolManager==null){
			JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
//			newSqlSymbolManager.setDialect(dialect);
			newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
			newSqlSymbolManager.setListeners(Arrays.asList(new DataQueryFilterListener()));
			this.sqlSymbolManager = newSqlSymbolManager;
		}
		
//		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.rowMapperFactory = new JFishRowMapperFactory(mappedEntryManager, jdbcResultSetGetter);

		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JFishSequenceNameManager();
		}
	}
	

	public DBDialect getDialect() {
		return dialect;
	}


	public void setJdbcParameterSetter(JdbcStatementParameterSetter jdbcParameterSetter) {
		this.jdbcParameterSetter = jdbcParameterSetter;
	}


	public JdbcStatementParameterSetter getJdbcParameterSetter() {
		return jdbcParameterSetter;
	}


	public MappedEntryManager getMappedEntryManager() {
		return mappedEntryManager;
	}

	public SQLSymbolManager getSqlSymbolManager() {
		return sqlSymbolManager;
	}

	public SequenceNameManager getSequenceNameManager() {
		return sequenceNameManager;
	}

	public DefaultDatabaseDialetManager getDatabaseDialetManager() {
		return databaseDialetManager;
	}

	public DbmConfig getDataBaseConfig() {
		return dataBaseConfig;
	}

	public RowMapperFactory getRowMapperFactory() {
		return rowMapperFactory;
	}

	public <T> T getService(Class<T> clazz) {
		Assert.notNull(clazz);
		return clazz.cast(getService(clazz.getName()));
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(String name) {
		Assert.hasText(name);
		return (T) services.get(name);
	}

	public <T> SimpleDbmInnerServiceRegistry register(T service) {
		return register(service.getClass().getName(), service);
	}

	public <T> SimpleDbmInnerServiceRegistry register(String name, T service) {
		Assert.hasText(name);
		Assert.notNull(service);
		services.put(name, service);
		return this;
	}

	public EntityValidator getEntityValidator() {
		return entityValidator;
	}


	public void setEntityValidator(EntityValidator entityValidator) {
		this.entityValidator = entityValidator;
	}


	public DbmTypeMapping getTypeMapping() {
		return typeMapping;
	}


	public void setTypeMapping(DbmTypeMapping typeMapping) {
		this.typeMapping = typeMapping;
	}
	

}
