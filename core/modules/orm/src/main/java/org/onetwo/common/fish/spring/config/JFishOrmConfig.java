package org.onetwo.common.fish.spring.config;

import javax.sql.DataSource;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishEntityManagerImpl;
import org.onetwo.common.fish.jpa.JPAMappedEntryBuilder;
import org.onetwo.common.fish.jpa.JPARelatedMappedEntryBuilder;
import org.onetwo.common.fish.orm.DBDialect;
import org.onetwo.common.fish.orm.InnerDBDialet;
import org.onetwo.common.fish.orm.JFishMappedEntryBuilder;
import org.onetwo.common.fish.orm.JFishRelatedMappedEntryManagerListener;
import org.onetwo.common.fish.orm.MappedEntryBuilder;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.orm.MappedEntryManagerListener;
import org.onetwo.common.fish.orm.MySQLDialect;
import org.onetwo.common.fish.orm.OracleDialect;
import org.onetwo.common.fish.spring.JFishDaoImpl;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.fish.spring.MutilMappedEntryManager;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.JFishJdbcTemplate;
import org.onetwo.common.jdbc.JFishJdbcTemplateProxy;
import org.onetwo.common.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
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
public class JFishOrmConfig implements ApplicationContextAware, InitializingBean{

	private ApplicationContext applicationContex;

	@Autowired
	private DataSource dataSource;

	private boolean watchSqlFile;
	
	private boolean logJdbcSql;

	
//	private JFishContextConfigurerListenerManager listenerManager= new JFishContextConfigurerListenerManager();
	
//	@Value("${app.cache}")
//	private String appCache;
	
//	@Autowired
//	private ConfigurableEnvironment env;
	
	private JFishOrmConfigurator jfishOrmConfigurator;
	
	private JFishConfigurer jfishConfigurer;
	
	public JFishOrmConfig(){
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContex = applicationContext;
//		listenerManager.addListener(pluginManager());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.jfishConfigurer = SpringUtils.getBean(applicationContex, JFishConfigurer.class);
		if(jfishConfigurer==null){
			this.jfishConfigurer = new JFishConfigurerAdapter();
		}
		this.jfishOrmConfigurator = SpringUtils.getBean(applicationContex, JFishOrmConfigurator.class);
		Assert.notNull(jfishOrmConfigurator);
		this.logJdbcSql = jfishOrmConfigurator.isLogJdbcSql();
		this.watchSqlFile = jfishOrmConfigurator.isWatchSqlFile();
	}

	public ApplicationContext getApplicationContex() {
		return applicationContex;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Bean
	public JFishDaoImplementor jfishDao() {
		JFishDaoImpl jfishDao = new JFishDaoImpl();
		jfishDao.setWatchSqlFile(watchSqlFile);
		jfishDao.setNamedParameterJdbcTemplate(namedJdbcTemplate());
		jfishDao.setJdbcTemplate(jdbcTemplate());
		jfishDao.setDataSource(dataSource);
//		jfishDao.setSqlSymbolManager(sqlSymbolManager());
//		jfishDao.setSequenceNameManager(sequenceNameManager());
		return jfishDao;
	}
	
	@Bean
	public JFishJdbcOperations jdbcTemplate(){
		JFishJdbcTemplate template = new JFishJdbcTemplate();
		template.setDataSource(dataSource);
		template.setDebug(logJdbcSql);
//		template.setNamedTemplate(namedJdbcTemplate());
//		template.setLogJdbcSql(logJdbcSql);

		if(logJdbcSql){
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
		MappedEntryManager em = SpringUtils.getBean(applicationContex, MappedEntryManager.class);
		if(em==null){
			MutilMappedEntryManager mem = new MutilMappedEntryManager();
			// em.setMappedEntryBuilder(mappedEntryBuilderList());
			if(!LangUtils.isEmpty(jfishOrmConfigurator.getModelBasePackages())){
				mem.setPackagesToScan(jfishOrmConfigurator.getModelBasePackages());
			}
			
			em = mem;
		}
		return em;
	}

	@Bean(name = "jfishDialect")
	public DBDialect jfishDialect() {
		InnerDBDialet dialet = (InnerDBDialet)jfishDao().getDialect();
//		dialet.setPrintSql(watchSqlFile);
		return dialet;
	}

//	@Bean
//	public SequenceNameManager sequenceNameManager() {
//		return new JFishSequenceNameManager();
//	}

	/*@Bean
	public SQLSymbolManager sqlSymbolManager() {
		SQLDialet sqlDialet = new DefaultSQLDialetImpl();
		JFishSQLSymbolManagerImpl sql = new JFishSQLSymbolManagerImpl(sqlDialet);
		sql.setDialect(jfishDialect());
		sql.setMappedEntryManager(mappedEntryManager());
		return sql;
	}*/

	@Bean
	public JFishEntityManager jfishEntityManager() {
		JFishEntityManager jem = jfishConfigurer.jfishEntityManager(jfishDao());
		if(jem==null){
			JFishEntityManagerImpl jemImpl = new JFishEntityManagerImpl();
			jemImpl.setJfishDao(jfishDao());
			jem = jemImpl;
		}
//		jem.setSequenceNameManager(sequenceNameManager());
//		jem.setSQLSymbolManager(sqlSymbolManager());
		return jem;
	}

	@Bean
	public MappedEntryBuilder jfishMappedEntryBuilder() {
		return new JFishMappedEntryBuilder();
	}

	@Bean
	public MappedEntryBuilder jpaMappedEntryBuilder() {
		return new JPAMappedEntryBuilder();
	}

	@Bean
	public MappedEntryBuilder jpaRelatedMappedEntryBuilder() {
		return new JPARelatedMappedEntryBuilder();
	}
	
	@Bean
	public MappedEntryManagerListener mappedEntryManagerListener(){
		MappedEntryManagerListener listener = new JFishRelatedMappedEntryManagerListener();
		return listener;
	}

	/*
	 * protected List<MappedEntryBuilder> mappedEntryBuilderList(){
	 * List<MappedEntryBuilder> list = new ArrayList<MappedEntryBuilder>();
	 * list.add(jfishMappedEntryBuilder());
	 * list.add(jpaRelatedMappedEntryBuilder()); //
	 * list.add(jpaMappedEntryBuilder()); return list; }
	 */

	@Bean(name = "mysqlDialect")
	public DBDialect mysqlDialet() {
		MySQLDialect dialet = new MySQLDialect(this.jfishOrmConfigurator.getDataBaseConfig());
		dialet.setAutoDetectIdStrategy(true);
//		dialet.setDbEventListenerManager(defaultDbEventListenerManager());
		return dialet;
	}

	@Bean(name = "oracleDialect")
	public DBDialect oracleDialet() {
		OracleDialect dialet = new OracleDialect(this.jfishOrmConfigurator.getDataBaseConfig());
		dialet.setAutoDetectIdStrategy(true);
//		dialet.setDbEventListenerManager(oracleDbEventListenerManager());
		return dialet;
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
