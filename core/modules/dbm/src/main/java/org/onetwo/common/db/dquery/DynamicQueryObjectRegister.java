package org.onetwo.common.db.dquery;

import java.util.stream.Stream;


import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.db.filequery.SqlFileScanner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.sql.SpringBasedSqlFileScanner;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.ClassUtils;


public class DynamicQueryObjectRegister implements /*FileNamedQueryFactoryListener, */ BeanDefinitionRegistryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

//	private NamespacePropertiesManager<? extends NamespaceProperty> namespacePropertiesManager;
	
//	private ApplicationContext applicationContext;
//	private QueryObjectFactory queryObjectFactory;
	
//	private QueryProvideManager baseEntityManager;
//	private FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory;
	
//	private boolean watchSqlFile = true;
	private SqlFileScanner sqlFileScanner = new SpringBasedSqlFileScanner(ClassUtils.getDefaultClassLoader());
	

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		/*BeanDefinition namedQueryMgrBeandef = BeanDefinitionBuilder.rootBeanDefinition(JFishNamedFileQueryManagerImpl.class)
												.addConstructorArgValue(JFishNamedSqlFileManager.createNamedSqlFileManager(watchSqlFile))
												.getBeanDefinition();
		registry.registerBeanDefinition(StringUtils.uncapitalize(JFishNamedFileQueryManagerImpl.class.getName()), namedQueryMgrBeandef);
		*/
		ResourceAdapter<?>[] sqlfiles = sqlFileScanner.scanMatchSqlFiles();
		Stream.of(sqlfiles).forEach(f->{
			final String fileName = f.getName();
			String className = StringUtils.substring(fileName, 0, fileName.length()-SqlFileScanner.JFISH_SQL_POSTFIX.length());
			final Class<?> interfaceClass = ReflectUtils.loadClass(className);
			BeanDefinition beandef = BeanDefinitionBuilder.rootBeanDefinition(JDKDynamicProxyCreator.class)
								.addConstructorArgValue(interfaceClass)
								.addPropertyValue("sqlFile", f)
								.setScope(BeanDefinition.SCOPE_SINGLETON)
//								.setRole(BeanDefinition.ROLE_APPLICATION)
								.getBeanDefinition();
			registry.registerBeanDefinition(className, beandef);
			logger.info("register dao bean: {} ", className);
		});
		
	}
	

	/*@Override
	public void afterPropertiesSet() throws Exception {
		if(this.baseEntityManager==null)
			this.baseEntityManager = SpringUtils.getBean(applicationContext, QueryProvideManager.class);
		if(this.fileNamedQueryFactory==null)
			this.fileNamedQueryFactory = SpringUtils.getBean(applicationContext, FileNamedQueryFactory.class);
		
		Assert.notNull(baseEntityManager);
		Assert.notNull(fileNamedQueryFactory);
		this.scanAndRegisterQueryObject();
	}*/

	/*protected void scanAndRegisterQueryObject() {
		Assert.notNull(queryObjectFactory);
		this.namespacePropertiesManager = fileNamedQueryFactory.getNamespacePropertiesManager();
		
		
		BeanFactory bf = null;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !SingletonBeanRegistry.class.isInstance(bf)){
			logger.warn("not SingletonBeanRegistry, ignore...");
			return ;
		}
		
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		Collection<?> namespacelist = namespacePropertiesManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		String beanName = null;
		QueryProvideManager cq = null;
		for(PropertiesNamespaceInfo<NamespaceProperty> nsp : (Collection<PropertiesNamespaceInfo<NamespaceProperty>>)namespacelist){
			if(nsp.isGlobal())
				continue;
			
			dqInterface = loadQueryClass(nsp);
			QueryProvider creator = dqInterface.getAnnotation(QueryProvider.class);
			if(creator!=null){
				if(StringUtils.isNotBlank(creator.value())){
					cq = SpringUtils.getBean(applicationContext, creator.value());
				}else{
					cq = SpringUtils.getHighestOrder(applicationContext, creator.beanClass());
				}
				if(cq==null){
					throw new BaseException("can not found QueryProvideManager for QueryProvider: " + creator.value()+", "+creator.beanClass());
				}
			}else{
				cq = baseEntityManager;
			}
			
			beanName = StringUtils.toClassName(dqInterface.getSimpleName());
			sbr.registerSingleton(beanName, this.queryObjectFactory.createQueryObject(cq, dqInterface));
			logger.info("register dynamic query dao {} ", beanName);
		}
	}*/

	/*private Class<?> loadQueryClass(PropertiesNamespaceInfo<NamespaceProperty> nsp){
		try {
			return ReflectUtils.loadClass(nsp.getNamespace());
		} catch (Exception e) {
			throw new BaseException("load class for query error: " + e.getMessage(), e);
		}
	}*/
	

	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	public void setQueryObjectFactory(QueryObjectFactory queryObjectFactory) {
		this.queryObjectFactory = queryObjectFactory;
	}


	public void setFileNamedQueryFactory(
			FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory) {
		this.fileNamedQueryFactory = fileNamedQueryFactory;
	}*/



}
