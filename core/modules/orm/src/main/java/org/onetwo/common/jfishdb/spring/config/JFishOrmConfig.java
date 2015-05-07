package org.onetwo.common.jfishdb.spring.config;

import javax.sql.DataSource;

import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.JFishJdbcTemplate;
import org.onetwo.common.jdbc.JFishJdbcTemplateProxy;
import org.onetwo.common.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.common.jdbc.JdbcUtils;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.jfishdb.JFishEntityManager;
import org.onetwo.common.jfishdb.JFishEntityManagerImpl;
import org.onetwo.common.jfishdb.dialet.AbstractDBDialect.DBMeta;
import org.onetwo.common.jfishdb.dialet.DBDialect;
import org.onetwo.common.jfishdb.dialet.DbmetaFetcher;
import org.onetwo.common.jfishdb.dialet.DefaultDatabaseDialetManager;
import org.onetwo.common.jfishdb.dialet.MySQLDialect;
import org.onetwo.common.jfishdb.dialet.OracleDialect;
import org.onetwo.common.jfishdb.orm.DataBaseConfig;
import org.onetwo.common.jfishdb.orm.DefaultDataBaseConfig;
import org.onetwo.common.jfishdb.orm.InnerDBDialet;
import org.onetwo.common.jfishdb.orm.MappedEntryManager;
import org.onetwo.common.jfishdb.spring.JFishDaoImpl;
import org.onetwo.common.jfishdb.spring.JFishDaoImplementor;
import org.onetwo.common.jfishdb.spring.JFishNamedFileQueryManagerImpl;
import org.onetwo.common.jfishdb.spring.MutilMappedEntryManager;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.context.ApplicationConfigKeys;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.spring.sql.StringTemplateLoaderFileSqlParser;
import org.onetwo.common.utils.LangUtils;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ImportResource({"classpath:jfish-spring.xml", "classpath:applicationContext.xml" })
//@Import(JFishProfiles.class)
public class JFishOrmConfig implements ApplicationContextAware, InitializingBean{

	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;

	@Value(ApplicationConfigKeys.BASE_PACKAGE_EXPR)
	protected String jfishBasePackages;

	@Autowired
	private JFishPropertyPlaceholder configHolder;
	
	public JFishOrmConfig(){
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
	public DataBaseConfig dataBaseConfig(){
		return new DefaultDataBaseConfig(configHolder);
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
		template.setDebug(dataBaseConfig().isLogSql());
//		template.setNamedTemplate(namedJdbcTemplate());
//		template.setLogJdbcSql(logJdbcSql);

		if(dataBaseConfig().isLogSql()){
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
		String[] packages = dataBaseConfig().getModelBasePackages();
		if(!LangUtils.isEmpty(packages)){
			em.scanPackages(packages);
		}
			
		return em;
	}
	
	@Bean
	public DefaultDatabaseDialetManager databaseDialetManager(){
		DefaultDatabaseDialetManager databaseDialetManager = new DefaultDatabaseDialetManager();
		databaseDialetManager.register(DataBase.MySQL.getName(), new MySQLDialect());
		databaseDialetManager.register(DataBase.Oracle.getName(), new OracleDialect());
		return databaseDialetManager;
	}
	
	@Bean
	public DBDialect dialect(){
		DataBase db = JdbcUtils.getDataBase(dataSource);
		DBDialect dialect = databaseDialetManager().getRegistered(db.getName());
		DBMeta dbmeta = DbmetaFetcher.create(dataSource).getDBMeta();
		LangUtils.cast(dialect, InnerDBDialet.class).setDbmeta(dbmeta);
		return dialect;
	}

/*	@Bean
	public DBDialect jfishDialect() {
		InnerDBDialet dialet = (InnerDBDialet)jfishDao().getDialect();
//		dialet.setPrintSql(watchSqlFile);
		return dialet;
	}*/


	@Bean
	public JFishEntityManager jfishEntityManager() {
		JFishEntityManagerImpl jem = new JFishEntityManagerImpl();
		jem.setJfishDao(jfishDao());
		//在afterpropertiesset里查找，避免循环依赖
//		jem.setFileNamedQueryFactory(fileNamedQueryFactory());
		return jem;
	}

	@Bean
	public FileNamedQueryFactory<JFishNamedFileQueryInfo> fileNamedQueryFactory(){
		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		FileNamedQueryFactory<JFishNamedFileQueryInfo> fq = new JFishNamedFileQueryManagerImpl(sqlFileManager(), listener);
		fq.initQeuryFactory(jfishEntityManager());
		return fq;
	}
	
	@Bean
	public JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager() {
		boolean watchSqlFile = configHolder.getPropertiesWraper().getBoolean(FileNamedQueryFactory.WATCH_SQL_FILE);
		DataBase db = JdbcUtils.getDataBase(dataSource);
//		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> listener = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
		JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlfileMgr = JFishNamedSqlFileManager.createDefaultJFishNamedSqlFileManager(db, watchSqlFile, listener);
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
