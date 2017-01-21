package org.onetwo.dbm.spring;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.dquery.DynamicQueryObjectRegister;
import org.onetwo.common.db.filequery.FileNamedQueryManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctions;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.DbmJdbcTemplate;
import org.onetwo.dbm.jdbc.DbmJdbcTemplateAspectProxy;
import org.onetwo.dbm.jdbc.DbmNamedJdbcTemplate;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.support.DbmDaoImpl;
import org.onetwo.dbm.support.DbmDaoImplementor;
import org.onetwo.dbm.support.DbmEntityManager;
import org.onetwo.dbm.support.DbmEntityManagerImpl;
import org.onetwo.dbm.support.SimpleDbmInnserServiceRegistry;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableConfigurationProperties({DataBaseConfig.class})
public class DbmSpringConfiguration implements ApplicationContextAware, InitializingBean/*, ImportAware*/ {

	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;
	
	private DataBase database;

	@Autowired(required=false)
	private DbmConfig dbmConfig;

	@Autowired(required=false)
	private Validator validator;
	
	public DbmSpringConfiguration(){
	}


	/*@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> annotationAttributes = importMetadata
				.getAnnotationAttributes(EnableJFishDbm.class.getName());
		AnnotationAttributes attrs = AnnotationAttributes.fromMap(annotationAttributes);
		this.database = (DataBase)attrs.get("database");
	}*/

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
		if(dataSource!=null){
			this.database = JdbcUtils.getDataBase(dataSource);
			DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(applicationContext);
			register.setDatabase(database);
			register.registerQueryBeans();
		}
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
	public DbmConfig defaultDbmConfig(){
		if(dbmConfig==null){
			dbmConfig = new DefaultDbmConfig();
		}
		return dbmConfig;
	}
	
	@Bean
	@Autowired
	public DbmEntityManager dbmEntityManager(DbmDaoImplementor dbmDao) {
		DbmEntityManagerImpl jem = new DbmEntityManagerImpl(dbmDao);
//		DbmDaoImplementor dbmDao = dbmDao();
//		jem.setDbmDao(dbmDao);
		jem.setSqlParamterPostfixFunctionRegistry(sqlParamterPostfixFunctionRegistry());
		//在afterpropertiesset里查找，避免循环依赖
//		jem.setFileNamedQueryFactory(fileNamedQueryFactory());

		BeanDefinitionRegistry registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
		DbmDaoCreateEvent event = new DbmDaoCreateEvent(dbmDao, registry);
		this.applicationContext.publishEvent(event);
		
		return jem;
	}
	
	@Bean
	public DataQueryFilterListener dataQueryFilterListener(){
		return new DataQueryFilterListener();
	}

	@Bean
	@Autowired
	public FileNamedQueryManager fileNamedQueryFactory(DbmEntityManager entityManager){
		/*JFishNamedSqlFileManager sqlFileManager = JFishNamedSqlFileManager.createNamedSqlFileManager(defaultDataBaseConfig().isWatchSqlFile());
		JFishNamedFileQueryManagerImpl fq = new JFishNamedFileQueryManagerImpl(sqlFileManager);
//		fq.initQeuryFactory(createQueryable);
		fq.setQueryProvideManager(jfishEntityManager());
		return fq;*/
		return entityManager.getFileNamedQueryManager();
	}
	
	@Bean
	public SimpleDbmInnserServiceRegistry dbmInnserServiceRegistry(){
		return SimpleDbmInnserServiceRegistry.createServiceRegistry(dataSource, validator, dbmConfig.getModelPackagesToScan());
	}
	
	@Bean
	@Autowired
	public DbmDaoImplementor dbmDao() {
		DbmDaoImpl jfishDao = new DbmDaoImpl(dataSource);
		jfishDao.setNamedParameterJdbcTemplate(namedJdbcTemplate());
		jfishDao.setJdbcTemplate(jdbcTemplate());
		jfishDao.setDataBaseConfig(defaultDbmConfig());
		jfishDao.setServiceRegistry(dbmInnserServiceRegistry());
		return jfishDao;
	}
	
	@Bean
	public DbmJdbcOperations jdbcTemplate(){
		DbmJdbcTemplate template = new DbmJdbcTemplate(dataSource, dbmInnserServiceRegistry().getJdbcParameterSetter());
		template.setDebug(defaultDbmConfig().isLogSql());

		if(defaultDbmConfig().isLogSql()){
			AspectJProxyFactory ajf = new AspectJProxyFactory(template);
			ajf.setProxyTargetClass(false);
			ajf.addAspect(DbmJdbcTemplateAspectProxy.class);
//			ajf.setTargetClass(JFishJdbcOperations.class);
			return ajf.getProxy();
		}
		
		return template;
	}
	
	@Bean
	public NamedJdbcTemplate namedJdbcTemplate(){
		DbmNamedJdbcTemplate template = new DbmNamedJdbcTemplate(jdbcTemplate());
		template.setJdbcParameterSetter(dbmInnserServiceRegistry().getJdbcParameterSetter());

		/*if(logJdbcSql){
			AspectJProxyFactory ajf = new AspectJProxyFactory(template);
			ajf.setProxyTargetClass(false);
			ajf.addAspect(JFishJdbcTemplateProxy.class);
			return ajf.getProxy();
		}*/
		
		return template;
	}

	@Bean
	public SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry(){
		return new SqlParamterPostfixFunctions();
	}
	
}
