package org.onetwo.common.spring.resource;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

/****
 * 扫描指定注解的类，并注册……
 * @author way
 *
 * @param <T>
 */
abstract public class AnnotationComponentScanner<T extends Annotation> {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private JFishResourcesScanner resourcesScanner = new JFishResourcesScanner();

	private BeanDefinitionRegistry registry;
	private String[] packagesToScan;
	private Class<T> annotationClass;
//	private AnnotationMetadata importingClassMetadata;
	
	
	public AnnotationComponentScanner(BeanDefinitionRegistry registry) {
		this.registry = registry;
//		this.importingClassMetadata = importingClassMetadata;
	}

	public AnnotationComponentScanner(ApplicationContext applicationContext) {
		this.registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
	}


	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@SuppressWarnings("unchecked")
	public boolean scanAndRegisterBeans() {
		String[] packagesToScan = getComponentPackages();
		if(ArrayUtils.isEmpty(packagesToScan)){
			logger.info("no packages config to scan for {} ....", annotationClass);
			return false;
		}
		logger.info("start to register @{} bean ....", annotationClass.getSimpleName());
		Collection<Class<T>> componentClassList = resourcesScanner.scan((metadataReader, res, index)->{
			if( metadataReader.getAnnotationMetadata().hasAnnotation(annotationClass.getName()) ){
				Class<T> cls = (Class<T>)ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				return cls;
			}
			return null;
		}, packagesToScan);
		
		for(Class<T> repositoryClass: componentClassList){
			this.doRegisterComponent(repositoryClass);
//			BeanDefinitionBuilder beandefBuilder = createBeanDefinitionBuilder(repositoryClass);
//					BeanDefinitionBuilder.rootBeanDefinition(AnnotationDynamicQueryHandlerProxyCreator.class)
//					.addConstructorArgValue(repositoryClass)
//					.addConstructorArgValue(methodCache)
//					.setScope(BeanDefinition.SCOPE_SINGLETON)
//					.setRole(BeanDefinition.ROLE_APPLICATION)
//					.getBeanDefinition()
		}
		boolean scaned = !componentClassList.isEmpty();
		return scaned;
	}
	
	protected void doRegisterComponent(Class<T> repositoryClass) {
		if(isComponentExist(repositoryClass)){
			return ;
		}
		
		if (repositoryClass.isInterface()) {
			throw new BaseException("interface can not register as a bean: " + repositoryClass);
		}

		String className = repositoryClass.getName();
		BeanDefinitionBuilder beandefBuilder = BeanDefinitionBuilder.rootBeanDefinition(repositoryClass);
		registry.registerBeanDefinition(className, beandefBuilder.getBeanDefinition());
		logger.info("register @{} bean: {} ", annotationClass.getSimpleName(), className);
	}
	
	final protected boolean isComponentExist(Class<T> repositoryClass) {
		String className = repositoryClass.getName();
		return registry.containsBeanDefinition(className);
	}
	
	protected String[] getComponentPackages() {
		String[] packs = this.packagesToScan;
//		if(LangUtils.isEmpty(packs) && importingClassMetadata!=null){
//			packs = new String[]{ClassUtils.getPackageName(importingClassMetadata.getClassName())};
//		}
		return packs;
	}

	public BeanDefinitionRegistry getRegistry() {
		return registry;
	}
}
