package org.onetwo.dbm.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctions;
import org.onetwo.common.db.filequery.spi.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.core.internal.DbmInterceptorManager;
import org.onetwo.dbm.core.internal.SessionCacheInterceptor;
import org.onetwo.dbm.core.spi.DbmInterceptor;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.dialet.DbmetaFetcher;
import org.onetwo.dbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.dbm.dialet.MySQLDialect;
import org.onetwo.dbm.dialet.OracleDialect;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.DbmJdbcOperationsProxy;
import org.onetwo.dbm.jdbc.DbmJdbcTemplate;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.jdbc.JdbcStatementParameterSetter;
import org.onetwo.dbm.jdbc.SpringJdbcResultSetGetter;
import org.onetwo.dbm.jdbc.SpringStatementParameterSetter;
import org.onetwo.dbm.jdbc.mapper.DbmRowMapperFactory;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.jpa.JPAMappedEntryBuilder;
import org.onetwo.dbm.jpa.JPASequenceNameManager;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DbmMappedEntryBuilder;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.mapping.EntityValidator;
import org.onetwo.dbm.mapping.MappedEntryBuilder;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MultiMappedEntryListener;
import org.onetwo.dbm.mapping.MutilMappedEntryManager;
import org.onetwo.dbm.query.JFishSQLSymbolManagerImpl;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
		serviceRegistry.initialize(context);
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
	
	

	@Autowired(required=false)
	private List<DbmInterceptor> interceptors;
	private DbmInterceptorManager interceptorManager;
	private DbmJdbcOperations dbmJdbcOperations;
	
	private SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry;
	
	public void initialize(DbmServiceRegistryCreateContext context){
		DataSource dataSource = context.getDataSource();
		
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDbmConfig();
		}
		if(jdbcParameterSetter==null){
			this.jdbcParameterSetter = new SpringStatementParameterSetter();
		}
		if(jdbcResultSetGetter==null){
			this.jdbcResultSetGetter = new SpringJdbcResultSetGetter();
		}
		if(sqlParamterPostfixFunctionRegistry==null){
			this.sqlParamterPostfixFunctionRegistry = new SqlParamterPostfixFunctions();
		}
		if(typeMapping==null){
			this.typeMapping = new DbmTypeMapping();
		}
		if(databaseDialetManager==null){
			this.databaseDialetManager = new DefaultDatabaseDialetManager();
			this.databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
			this.databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		}
		
		Validator validator = context.getEntityValidator();
		if(validator!=null){
			this.entityValidator = new Jsr303EntityValidator(validator);
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
			MutilMappedEntryManager entryManager = new MutilMappedEntryManager();
//			this.mappedEntryManager.initialize();
			
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new DbmMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(this);
			builder.initialize();
			builders.add(builder);
//			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
			entryManager.setMappedEntryBuilder(ImmutableList.copyOf(builders));
			this.mappedEntryManager = entryManager;
			
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
		
		if(interceptors==null){
			interceptors = Lists.newArrayList();
		}
		//add
		if(this.interceptorManager==null){
			List<DbmInterceptor> interceptors = Lists.newArrayList();
			interceptors.add(new SessionCacheInterceptor(context.getSessionFactory()));
			if(this.interceptors!=null){
				interceptors.addAll(this.interceptors);
			}
			DbmInterceptorManager interceptorManager = new DbmInterceptorManager();
			interceptorManager.setInterceptors(ImmutableList.copyOf(interceptors));
			this.interceptorManager = interceptorManager;
		}
		if(this.dbmJdbcOperations==null){
			DbmJdbcTemplate jdbcTemplate = new DbmJdbcTemplate(dataSource, getJdbcParameterSetter());
			AspectJProxyFactory ajf = new AspectJProxyFactory(jdbcTemplate);
			ajf.setProxyTargetClass(true);
			ajf.addAspect(new DbmJdbcOperationsProxy(interceptorManager, jdbcTemplate));
			this.dbmJdbcOperations = ajf.getProxy();
		}
	}

	public DBDialect getDialect() {
		return dialect;
	}

	public DbmJdbcOperations getDbmJdbcOperations() {
		return dbmJdbcOperations;
	}
	public void setDbmJdbcOperations(DbmJdbcOperations dbmJdbcOperations) {
		this.dbmJdbcOperations = dbmJdbcOperations;
	}
	public void setJdbcParameterSetter(JdbcStatementParameterSetter jdbcParameterSetter) {
		this.jdbcParameterSetter = jdbcParameterSetter;
	}


	public JdbcStatementParameterSetter getJdbcParameterSetter() {
		return jdbcParameterSetter;
	}

	public DbmInterceptorManager getInterceptorManager() {
		return interceptorManager;
	}
	public void setInterceptorManager(DbmInterceptorManager interceptorManager) {
		this.interceptorManager = interceptorManager;
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

	
	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry() {
		return sqlParamterPostfixFunctionRegistry;
	}
	public void setSqlParamterPostfixFunctionRegistry(
			SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry) {
		this.sqlParamterPostfixFunctionRegistry = sqlParamterPostfixFunctionRegistry;
	}


	public static class DbmServiceRegistryCreateContext {
		final private DbmSessionFactory sessionFactory;
		final private ApplicationContext applicationContext;
		public DbmServiceRegistryCreateContext(DbmSessionFactory sessionFactory) {
			this(null, sessionFactory);
		}
		public DbmServiceRegistryCreateContext(ApplicationContext applicationContext, DbmSessionFactory sessionFactory) {
			super();
			this.sessionFactory = sessionFactory;
			this.applicationContext = applicationContext;
		}
		public DataSource getDataSource() {
			return sessionFactory.getDataSource();
		}
		public DbmSessionFactory getSessionFactory() {
			return sessionFactory;
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
			result = prime
					* result
					+ ((sessionFactory == null) ? 0 : sessionFactory.hashCode());
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
			if (sessionFactory == null) {
				if (other.sessionFactory != null)
					return false;
			} else if (!sessionFactory.equals(other.sessionFactory))
				return false;
			return true;
		}
		
	}

}
