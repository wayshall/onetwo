package org.onetwo.common.jfishdbm.spring;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.onetwo.common.db.filequery.FileNamedQueryManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctions;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.jfishdbm.jdbc.JFishJdbcOperations;
import org.onetwo.common.jfishdbm.jdbc.JFishJdbcTemplate;
import org.onetwo.common.jfishdbm.jdbc.JFishJdbcTemplateAspectProxy;
import org.onetwo.common.jfishdbm.jdbc.JFishNamedJdbcTemplate;
import org.onetwo.common.jfishdbm.jdbc.NamedJdbcTemplate;
import org.onetwo.common.jfishdbm.mapping.DataBaseConfig;
import org.onetwo.common.jfishdbm.mapping.DefaultDataBaseConfig;
import org.onetwo.common.jfishdbm.support.DaoFactory;
import org.onetwo.common.jfishdbm.support.DbmDaoImpl;
import org.onetwo.common.jfishdbm.support.DbmDaoImplementor;
import org.onetwo.common.jfishdbm.support.DbmEntityManager;
import org.onetwo.common.jfishdbm.support.DbmEntityManagerImpl;
import org.onetwo.common.jfishdbm.support.SimpleDbmInnserServiceRegistry;
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
//@EnableConfigurationProperties({DataBaseConfig.class})
public class JFishdbmSpringConfiguration implements ApplicationContextAware, InitializingBean{

	private ApplicationContext applicationContext;

	@Autowired
	private DataSource dataSource;

	@Autowired(required=false)
	private DataBaseConfig dataBaseConfig;

	@Autowired(required=false)
	private Validator validator;
	
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
	public DbmEntityManager jfishEntityManager() {
		DbmEntityManagerImpl jem = new DbmEntityManagerImpl();
		jem.setDbmDao(jfishDao());
		jem.setSqlParamterPostfixFunctionRegistry(sqlParamterPostfixFunctionRegistry());
		//在afterpropertiesset里查找，避免循环依赖
//		jem.setFileNamedQueryFactory(fileNamedQueryFactory());
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
		NamedJdbcTemplate template = new JFishNamedJdbcTemplate(jdbcTemplate());

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
