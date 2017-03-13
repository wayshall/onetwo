package org.onetwo.dbm.support;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
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
import org.onetwo.dbm.jdbc.mapper.DbmRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.jpa.JPASequenceNameManager;
import org.onetwo.dbm.jpa.JPAMappedEntryBuilder;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.mapping.EntityValidator;
import org.onetwo.dbm.mapping.DbmMappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MultiMappedEntryListener;
import org.onetwo.dbm.mapping.MutilMappedEntryManager;
import org.onetwo.dbm.query.JFishSQLSymbolManagerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class SimpleDbmInnerServiceRegistry {

	final private static LoadingCache<DbmServiceRegistryCreateContext, SimpleDbmInnerServiceRegistry> SERVICE_REGISTRY_MAPPER = CacheBuilder.newBuilder()
																						.weakKeys()
																						.weakValues()
																						.build(new CacheLoader<DbmServiceRegistryCreateContext, SimpleDbmInnerServiceRegistry>() {

																							@Override
																							public SimpleDbmInnerServiceRegistry load(DbmServiceRegistryCreateContext ctx) throws Exception {
																								return SimpleDbmInnerServiceRegistry.createServiceRegistry(ctx);
																							}
																							
																						});

	public static SimpleDbmInnerServiceRegistry obtainServiceRegistry(DbmServiceRegistryCreateContext context){
		try {
			return SERVICE_REGISTRY_MAPPER.get(context);
		} catch (ExecutionException e) {
			throw new BaseException("obtain SimpleDbmInnerServiceRegistry error: " + e.getMessage(), e);
		}
	}
	private static SimpleDbmInnerServiceRegistry createServiceRegistry(DbmServiceRegistryCreateContext context){
		SimpleDbmInnerServiceRegistry serviceRegistry = new SimpleDbmInnerServiceRegistry();
		serviceRegistry.initialize(context.getDataSource());
		Validator validator = context.getEntityValidator();
		if(validator!=null){
			serviceRegistry.setEntityValidator(new Jsr303EntityValidator(validator));
		}
		return serviceRegistry;
	}
	
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
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
	
	public void initialize(DataSource dataSource){
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
			MappedEntryBuilder builder = new DbmMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
//			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
			_mappedEntryManager.setMappedEntryBuilder(ImmutableList.copyOf(builders));
			this.mappedEntryManager = _mappedEntryManager;
			
		}
		MultiMappedEntryListener ml = new MultiMappedEntryListener();
		mappedEntryManager.setMappedEntryManagerListener(ml);
		
		//init sql symbol
		if(sqlSymbolManager==null){
			JFishSQLSymbolManagerImpl newSqlSymbolManager = JFishSQLSymbolManagerImpl.create();
//			newSqlSymbolManager.setDialect(dialect);
			newSqlSymbolManager.setMappedEntryManager(mappedEntryManager);
			newSqlSymbolManager.setListeners(Arrays.asList(new DataQueryFilterListener()));
			this.sqlSymbolManager = newSqlSymbolManager;
		}
		
//		this.mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		this.rowMapperFactory = new DbmRowMapperFactory(mappedEntryManager, jdbcResultSetGetter);

		if(this.sequenceNameManager==null){
			this.sequenceNameManager = new JPASequenceNameManager();
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

	
	public static class DbmServiceRegistryCreateContext {
		final private DataSource dataSource;
		final private ApplicationContext applicationContext;
		public DbmServiceRegistryCreateContext(DataSource dataSource) {
			this(null, dataSource);
		}
		public DbmServiceRegistryCreateContext(ApplicationContext applicationContext, DataSource dataSource) {
			super();
			this.dataSource = dataSource;
			this.applicationContext = applicationContext;
		}
		public DataSource getDataSource() {
			return dataSource;
		}
		public ApplicationContext getApplicationContext() {
			return applicationContext;
		}
		public Validator getEntityValidator(){
			if(applicationContext==null){
				return null;
			}
			return SpringUtils.getBean(applicationContext, Validator.class);
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((dataSource == null) ? 0 : dataSource.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DbmServiceRegistryCreateContext other = (DbmServiceRegistryCreateContext) obj;
			if (dataSource == null) {
				if (other.dataSource != null)
					return false;
			} else if (!dataSource.equals(other.dataSource))
				return false;
			return true;
		}
		
	}

}
