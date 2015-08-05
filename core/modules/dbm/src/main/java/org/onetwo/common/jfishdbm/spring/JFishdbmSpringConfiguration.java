package org.onetwo.common.jfishdbm.spring;

import javax.sql.DataSource;

import org.onetwo.common.db.dquery.DynamicQueryObjectRegister;
import org.onetwo.common.db.filequery.FileNamedQueryFactory;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.JFishJdbcTemplate;
import org.onetwo.common.jdbc.JFishJdbcTemplateProxy;
import org.onetwo.common.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.common.jdbc.JdbcUtils;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.common.jfishdbm.dialet.DBDialect;
import org.onetwo.common.jfishdbm.dialet.DbmetaFetcher;
import org.onetwo.common.jfishdbm.dialet.DefaultDatabaseDialetManager;
import org.onetwo.common.jfishdbm.dialet.MySQLDialect;
import org.onetwo.common.jfishdbm.dialet.OracleDialect;
import org.onetwo.common.jfishdbm.mapping.DataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.DefaultDataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.MappedEntryManager;
import org.onetwo.common.jfishdbm.mapping.MutilMappedEntryManager;
import org.onetwo.common.jfishdbm.query.JFishNamedFileQueryManagerImpl;
import org.onetwo.common.jfishdbm.support.JFishDaoImpl;
import org.onetwo.common.jfishdbm.support.JFishDaoImplementor;
import org.onetwo.common.jfishdbm.support.JFishEntityManager;
import org.onetwo.common.jfishdbm.support.JFishEntityManagerImpl;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager.DefaultJFishNamedSqlFileManager;
import org.onetwo.common.spring.sql.StringTemplateLoaderFileSqlParser;
import org.onetwo.common.utils.LangUtils;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ImportResource({"classpath:jfish-spring.xml", "classpath:applicationContext.xml" })
//@Import(JFishProfiles.class)
public class JFishdbmSpringConfiguration implements ApplicationContextAware, InitializingBean{
//	public static final String JFISH_DBM_SQLFILE_WATCH = "jfish.dbm.sqlfile.watch";

	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;

	/*@Value(ApplicationConfigKeys.BASE_PACKAGE_EXPR)
	protected String jfishBasePackages;*/

	/*@Value(JFISH_DBM_SQLFILE_WATCH)
	private boolean watchSqlFile;*/

	/*@Autowired
	private JFishPropertyPlaceholder configHolder;*/
	
	/*@Autowired
	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;*/
	@Autowired(required=false)
	private DynamicQueryObjectRegister dynamicQueryObjectRegister;

	@Autowired(required=false)
	private SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry;
	
	@Autowired(required=false)
	private DataBaseConfig dataBaseConfig;
	
	/***
	 * 避免启动时JFishEntityManager延迟加载
	 */
	@Autowired
	private JFishEntityManager jfishEntityManager;
	
	public JFishdbmSpringConfiguration(){
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public ApplicationContext getApplicationContex() {
		return applicationContext;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Bean
	public DataBaseConfig defaultDataBaseConfig(){
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDataBaseConfig();
		}
		return dataBaseConfig;
	}
	@Bean
	public JFishDaoImplementor jfishDao() {
		JFishDaoImpl jfishDao = new JFishDaoImpl(dataSource);
		jfishDao.setNamedParameterJdbcTemplate(namedJdbcTemplate());
		jfishDao.setJdbcTemplate(jdbcTemplate());
		jfishDao.setDataSource(dataSource);
		jfishDao.setMappedEntryManager(mappedEntryManager());
		jfishDao.setDialect(dialect());
//		jfishDao.setSqlSymbolManager(sqlSymbolManager());
//		jfishDao.setSequenceNameManager(sequenceNameManager());
		return jfishDao;
	}
	
	@Bean
	public JFishJdbcOperations jdbcTemplate(){
		JFishJdbcTemplate template = new JFishJdbcTemplate();
		template.setDataSource(dataSource);
		template.setDebug(defaultDataBaseConfig().isLogSql());
//		template.setNamedTemplate(namedJdbcTemplate());
//		template.setLogJdbcSql(logJdbcSql);

		if(defaultDataBaseConfig().isLogSql()){
			AspectJProxyFactory ajf = new AspectJProxyFactory(template);
			ajf.setProxyTargetClass(false);
			ajf.addAspect(JFishJdbcTemplateProxy.class);
//			ajf.setTargetClass(JFishJdbcOperations.class);
			return ajf.getProxy();
		}
		
		return template;
	}
	
	@Bean
	public NamedJdbcTemplate namedJdbcTemplate(){
		NamedJdbcTemplate template = new JFishNamedJdbcTemplate(jdbcTemplate());

		/*if(logJdbcSql){
			AspectJProxyFactory ajf = new AspectJProxyFactory(template);
			ajf.setProxyTargetClass(false);
			ajf.addAspect(JFishJdbcTemplateProxy.class);
			return ajf.getProxy();
		}*/
		
		return template;
	}
	
	/*@Bean
	public JFishDaoProxy jfishDaoProxy(){
		JFishDaoProxy jdaoProxy = new JFishDaoProxy();
		return jdaoProxy;
	}*/

	@Bean
	public MappedEntryManager mappedEntryManager() {
		MappedEntryManager em = new MutilMappedEntryManager(dialect());
		em.initialize();
		// em.setMappedEntryBuilder(mappedEntryBuilderList());
		String[] packages = defaultDataBaseConfig().getModelBasePackages();
		if(!LangUtils.isEmpty(packages)){
			em.scanPackages(packages);
		}
			
		return em;
	}
	
	@Bean
	public DefaultDatabaseDialetManager databaseDialetManager(){
		DefaultDatabaseDialetManager databaseDialetManager = new DefaultDatabaseDialetManager();
		databaseDialetManager.register(new MySQLDialect());
		databaseDialetManager.register(new OracleDialect());
		return databaseDialetManager;
	}
	
	@Bean
	public DBDialect dialect(){
//		DataBase db = JdbcUtils.getDataBase(dataSource);
		DBMeta dbmeta = DbmetaFetcher.create(dataSource).getDBMeta();
		
		DBDialect dialect = databaseDialetManager().getRegistered(dbmeta.getDbName());
//		LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
		dialect.getDbmeta().setVersion(dbmeta.getVersion());
		return dialect;
	}



	@Bean
	public JFishEntityManager jfishEntityManager() {
		JFishEntityManagerImpl jem = new JFishEntityManagerImpl();
		jem.setJfishDao(jfishDao());
		jem.setSqlParamterPostfixFunctionRegistry(sqlParamterPostfixFunctionRegistry);
		//在afterpropertiesset里查找，避免循环依赖
//		jem.setFileNamedQueryFactory(fileNamedQueryFactory());
		return jem;
	}

	@Bean
	public FileNamedQueryFactory<JFishNamedFileQueryInfo> fileNamedQueryFactory(){
//		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		FileNamedQueryFactory<JFishNamedFileQueryInfo> fq = new JFishNamedFileQueryManagerImpl(sqlFileManager());
		fq.initQeuryFactory(jfishEntityManager());
		return fq;
	}
	
	/***
	 * sql文件管理
	 * @return
	 */
	@Bean
	public JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager() {
		DataBase db = JdbcUtils.getDataBase(dataSource);
//		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> listener = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
		JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlfileMgr = new DefaultJFishNamedSqlFileManager(db, defaultDataBaseConfig().isWatchSqlFile(), listener);
//		sqlfileMgr.setSqlFileParser(sqlFileParser);
		return sqlfileMgr;
	}
	
/****
	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		CacheManager cache = null;
		Resource res = SpringUtils.newClassPathResource("cache/ehcache.xml");
		if(res.exists()){
			cache = ehcacheCacheManager(res);
		}else{
			cache = jfishSimpleCacheManager();
		}
		
		return cache;
	}

	@Bean(name = "jfishSimpleCacheManager")
	public CacheManager jfishSimpleCacheManager() {
		JFishSimpleCacheManagerImpl cache = new JFishSimpleCacheManagerImpl();
		
		return cache;
	}
	
	protected CacheManager ehcacheCacheManager(Resource configLocation){
		net.sf.ehcache.CacheManager cm = null;
		if(AbstractJFishAnnotationConfig.class.isInstance(applicationContex)){
			AbstractJFishAnnotationConfig jfishWebapp = (AbstractJFishAnnotationConfig) applicationContex;
			cm = jfishWebapp.registerAndGetBean(EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}else{
			cm = SpringUtils.registerBean(applicationContex, EhCacheManagerFactoryBean.class, "configLocation", configLocation);
		}
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(cm);
		return cacheManager;
	}
****/
}
