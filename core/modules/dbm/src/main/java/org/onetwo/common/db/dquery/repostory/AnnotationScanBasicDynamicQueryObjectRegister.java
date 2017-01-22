package org.onetwo.common.db.dquery.repostory;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.dquery.DynamicMethod;
import org.onetwo.common.db.dquery.DynamicQueryObjectRegistor;
import org.onetwo.common.db.dquery.annotation.QueryRepository;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AnnotationScanBasicDynamicQueryObjectRegister implements DynamicQueryObjectRegistor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private JFishResourcesScanner resourcesScanner = new JFishResourcesScanner();

	private LoadingCache<Method, DynamicMethod> methodCache = CacheBuilder.newBuilder()
																.build(new CacheLoader<Method, DynamicMethod>() {
																	@Override
																	public DynamicMethod load(Method method) throws Exception {
																		return DynamicMethod.newDynamicMethod(method);
																	}
																});

//	private SqlFileScanner sqlFileScanner = new SpringBasedSqlFileScanner(ClassUtils.getDefaultClassLoader());
	private BeanDefinitionRegistry registry;
//	private ApplicationContext applicationContext;
	private String[] packagesToScan;
	
	public AnnotationScanBasicDynamicQueryObjectRegister(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public AnnotationScanBasicDynamicQueryObjectRegister(ApplicationContext applicationContext) {
		this.registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public boolean registerQueryBeans() {
		if(ArrayUtils.isEmpty(packagesToScan)){
			logger.info("no packages config to scan for DbmRepository ....");
			return false;
		}
		logger.info("start to register dao bean ....");
		List<Class<?>> dbmRepositoryClasses = resourcesScanner.scan((metadataReader, res, index)->{
			if(metadataReader.getAnnotationMetadata().hasAnnotation(QueryRepository.class.getName())){
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				return cls;
			}
			return null;
		}, packagesToScan);
		
		for(Class<?> repositoryClass: dbmRepositoryClasses){
			String className = repositoryClass.getName();
			if(registry.containsBeanDefinition(className)){
				continue;
			}
			BeanDefinition beandef = BeanDefinitionBuilder.rootBeanDefinition(AnnotationBasicJDKDynamicProxyCreator.class)
					.addConstructorArgValue(repositoryClass)
					.addConstructorArgValue(methodCache)
					.setScope(BeanDefinition.SCOPE_SINGLETON)
//					.setRole(BeanDefinition.ROLE_APPLICATION)
					.getBeanDefinition();
			registry.registerBeanDefinition(className, beandef);
			logger.info("register dao bean: {} ", className);
		}
		boolean scaned = !dbmRepositoryClasses.isEmpty();
		return scaned;
	}
}
