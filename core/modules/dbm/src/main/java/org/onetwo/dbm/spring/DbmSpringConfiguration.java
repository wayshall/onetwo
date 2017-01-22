package org.onetwo.dbm.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.dquery.DynamicQueryObjectRegisterListener;
import org.onetwo.common.db.filequery.FileNamedQueryManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctions;
import org.onetwo.common.db.filter.annotation.DataQueryFilterListener;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.DbmJdbcTemplate;
import org.onetwo.dbm.jdbc.DbmJdbcTemplateAspectProxy;
import org.onetwo.dbm.jdbc.DbmNamedJdbcTemplate;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.support.DbmDaoImpl;
import org.onetwo.dbm.support.DbmDaoImplementor;
import org.onetwo.dbm.support.DbmEntityManager;
import org.onetwo.dbm.support.DbmEntityManagerImpl;
import org.onetwo.dbm.support.SimpleDbmInnerServiceRegistry;
import org.onetwo.dbm.utils.DbmUtils;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
//@EnableConfigurationProperties({DataBaseConfig.class})
public class DbmSpringConfiguration implements ApplicationContextAware, InitializingBean, ImportAware {

	/*volatile private static boolean dbmRepostoryScaned = false;
	
	public static boolean isDbmRepostoryScaned() {
		return dbmRepostoryScaned;
	}
	public static void setDbmRepostoryScaned(boolean dbmRepostoryScaned) {
		DbmSpringConfiguration.dbmRepostoryScaned = dbmRepostoryScaned;
	}*/

	private ApplicationContext applicationContext;

	@Autowired(required=false)
	private DbmConfig dbmConfig;

	@Autowired(required=false)
	private Validator validator;
	
//	private String[] packagesToScan;
	private List<String> packageNames = new ArrayList<String>();
	
	public DbmSpringConfiguration(){
	}


	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		/*Map<String, Object> annotationAttributes = importMetadata.getAnnotationAttributes(EnableDbm.class.getName());
		AnnotationAttributes attrs = AnnotationAttributes.fromMap(annotationAttributes);
		if(attrs==null){
			return ;
		}
		String[] packagesToScan = attrs.getStringArray("packagesToScan");
		if(ArrayUtils.isEmpty(packagesToScan)){
			packageNames.addAll(DbmUtils.scanEnableDbmPackages(applicationContext));
		}*/
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		Springs.initApplicationIfNotInitialized(applicationContext);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	/*@Bean
	public AnnotationScanBasicDynamicQueryObjectRegisterTrigger annotationScanBasicDynamicQueryObjectRegisterTrigger(){
		AnnotationScanBasicDynamicQueryObjectRegisterTrigger register = new AnnotationScanBasicDynamicQueryObjectRegisterTrigger(applicationContext);
		return register;
	}*/
	
	@Bean
	public DynamicQueryObjectRegisterListener dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener();
	}
	
	/*@Bean
	public AnnotationScanBasicDynamicQueryObjectRegisterTrigger annotationScanBasicDynamicQueryObjectRegisterTrigger(){
		AnnotationScanBasicDynamicQueryObjectRegisterTrigger register = new AnnotationScanBasicDynamicQueryObjectRegisterTrigger(applicationContext);
		register.setPackageNames(getAllDbmPackageNames());
		return register;
	}*/
	
	public ApplicationContext getApplicationContex() {
		return applicationContext;
	}

	/*@Bean
	public AnnotationScanBasicDynamicQueryObjectRegister dynamicQueryObjectRegister(){
		AnnotationScanBasicDynamicQueryObjectRegister register = new AnnotationScanBasicDynamicQueryObjectRegister(this.applicationContext);
		return register;
	}*/

	public DbmConfig defaultDbmConfig(){
//		DbmConfigFactory dbmConfigFactory = Springs.getInstance().getBean(DbmConfig.class);
		if(dbmConfig==null){
			this.dbmConfig = new DefaultDbmConfig();
		}
		return this.dbmConfig;
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
	public SimpleDbmInnerServiceRegistry dbmInnerServiceRegistry(DataSource dataSource){
		return SimpleDbmInnerServiceRegistry.createServiceRegistry(dataSource, validator, getAllDbmPackageNames());
	}
	
	private Collection<String> getAllDbmPackageNames(){
		Collection<String> packageNames = new HashSet<>();
		packageNames.addAll(this.packageNames);
		String[] modelPackages = defaultDbmConfig().getModelPackagesToScan();
		if(ArrayUtils.isNotEmpty(modelPackages)){
			packageNames.addAll(Arrays.asList(modelPackages));
		}
		packageNames.addAll(DbmUtils.scanEnableDbmPackages(applicationContext));
		packageNames.addAll(DbmUtils.scanDbmPackages(applicationContext));
		return packageNames;
	}
	
	@Bean
	@Autowired
	public DbmDaoImplementor dbmDao(DataSource dataSource) {
		DbmDaoImpl jfishDao = new DbmDaoImpl(dataSource);
		jfishDao.setNamedParameterJdbcTemplate(namedJdbcTemplate(dataSource));
		jfishDao.setJdbcTemplate(jdbcTemplate(dataSource));
		jfishDao.setDataBaseConfig(defaultDbmConfig());
		jfishDao.setServiceRegistry(dbmInnerServiceRegistry(dataSource));
		return jfishDao;
	}
	
	@Bean
	@Autowired
	public DbmJdbcOperations jdbcTemplate(DataSource dataSource){
		DbmJdbcTemplate template = new DbmJdbcTemplate(dataSource, dbmInnerServiceRegistry(dataSource).getJdbcParameterSetter());
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
	@Autowired
	public NamedJdbcTemplate namedJdbcTemplate(DataSource dataSource){
		DbmNamedJdbcTemplate template = new DbmNamedJdbcTemplate(jdbcTemplate(dataSource));
		template.setJdbcParameterSetter(dbmInnerServiceRegistry(dataSource).getJdbcParameterSetter());

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
