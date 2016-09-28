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
import org.onetwo.dbm.jdbc.JFishJdbcOperations;
import org.onetwo.dbm.jdbc.JFishJdbcTemplate;
import org.onetwo.dbm.jdbc.JFishJdbcTemplateAspectProxy;
import org.onetwo.dbm.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.mapping.DataBaseConfig;
import org.onetwo.dbm.mapping.DefaultDataBaseConfig;
import org.onetwo.dbm.support.DaoFactory;
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
//@ImportResource({"classpath:jfish-spring.xml", "classpath:applicationContext.xml" })
//@Import(JFishProfiles.class)
//@EnableConfigurationProperties({DataBaseConfig.class})
public class JFishdbmSpringConfiguration implements ApplicationContextAware, InitializingBean/*, ImportAware*/ {

	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;
	
	private DataBase database;

	@Autowired(required=false)
	private DataBaseConfig dataBaseConfig;

	@Autowired(required=false)
	private Validator validator;
	
	public JFishdbmSpringConfiguration(){
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
	public DataBaseConfig defaultDataBaseConfig(){
		if(dataBaseConfig==null){
			dataBaseConfig = new DefaultDataBaseConfig();
		}
		return dataBaseConfig;
	}
	
	@Bean
	public DbmEntityManager jfishEntityManager() {
		DbmEntityManagerImpl jem = new DbmEntityManagerImpl();
		DbmDaoImplementor dbmDao = jfishDao();
		jem.setDbmDao(dbmDao);
		jem.setSqlParamterPostfixFunctionRegistry(sqlParamterPostfixFunctionRegistry());
		//在afterpropertiesset里查找，避免循环依赖
//		jem.setFileNamedQueryFactory(fileNamedQueryFactory());

		if(this.database==null){
			BeanDefinitionRegistry registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
			DbmDaoCreateEvent event = new DbmDaoCreateEvent(dbmDao, registry);
			this.applicationContext.publishEvent(event);
		}
		return jem;
	}
	
	@Bean
	public DataQueryFilterListener dataQueryFilterListener(){
		return new DataQueryFilterListener();
	}

	@Bean
	@Autowired
	public FileNamedQueryManager fileNamedQueryFactory(){
		/*JFishNamedSqlFileManager sqlFileManager = JFishNamedSqlFileManager.createNamedSqlFileManager(defaultDataBaseConfig().isWatchSqlFile());
		JFishNamedFileQueryManagerImpl fq = new JFishNamedFileQueryManagerImpl(sqlFileManager);
//		fq.initQeuryFactory(createQueryable);
		fq.setQueryProvideManager(jfishEntityManager());
		return fq;*/
		return jfishEntityManager().getFileNamedQueryManager();
	}
	
	@Bean
	public SimpleDbmInnserServiceRegistry dbmInnserServiceRegistry(){
		return DaoFactory.createServiceRegistry(dataSource, validator);
	}
	
	@Bean
	@Autowired
	public DbmDaoImplementor jfishDao() {
		DbmDaoImpl jfishDao = new DbmDaoImpl(dataSource);
		jfishDao.setNamedParameterJdbcTemplate(namedJdbcTemplate());
		jfishDao.setJdbcTemplate(jdbcTemplate());
		jfishDao.setDataBaseConfig(defaultDataBaseConfig());
		jfishDao.setServiceRegistry(dbmInnserServiceRegistry());
//		jfishDao.setMappedEntryManager(mappedEntryManager());
//		jfishDao.setDialect(dialect());
//		jfishDao.setSqlSymbolManager(sqlSymbolManager());
//		jfishDao.setSequenceNameManager(sequenceNameManager());
		return jfishDao;
	}
	
	@Bean
	public JFishJdbcOperations jdbcTemplate(){
		JFishJdbcTemplate template = new JFishJdbcTemplate();
		template.setDataSource(dataSource);
		template.setDebug(defaultDataBaseConfig().isLogSql());
		template.setJdbcParameterSetter(dbmInnserServiceRegistry().getJdbcParameterSetter());
//		template.setNamedTemplate(namedJdbcTemplate());
//		template.setLogJdbcSql(logJdbcSql);

		if(defaultDataBaseConfig().isLogSql()){
			AspectJProxyFactory ajf = new AspectJProxyFactory(template);
			ajf.setProxyTargetClass(false);
			ajf.addAspect(JFishJdbcTemplateAspectProxy.class);
//			ajf.setTargetClass(JFishJdbcOperations.class);
			return ajf.getProxy();
		}
		
		return template;
	}
	
	@Bean
	public NamedJdbcTemplate namedJdbcTemplate(){
		JFishNamedJdbcTemplate template = new JFishNamedJdbcTemplate(jdbcTemplate());
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
