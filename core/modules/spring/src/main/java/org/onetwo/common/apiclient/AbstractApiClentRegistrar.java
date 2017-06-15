package org.onetwo.common.apiclient;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.AnnotationMetadataHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractApiClentRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, ResourceLoaderAware {

	public static final String ATTRS_BASE_URL = "baseUrl";
	public static final String ATTRS_NAME = "name";
	public static final String ATTRS_PATH = "path";
	
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ResourceLoader resourceLoader;
	private ClassLoader classLoader;
	private AnnotationMetadataHelper annotationMetadataHelper;
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/****
	 * EnableWechatClient
	 * 
	 * @author wayshall
	 * @return
	 */
	abstract protected Class<? extends Annotation> getImportingAnnotationClass();
	
	/****
	 * 
	 * @author wayshall
	 * @return
	 */
	abstract protected Class<? extends Annotation> getApiClientTagAnnotationClass();
	
	public AnnotationMetadataHelper getAnnotationMetadataHelper(AnnotationMetadata importingClassMetadata){
		AnnotationMetadataHelper annotationMetadataHelper = this.annotationMetadataHelper;
		if(annotationMetadataHelper==null){
			annotationMetadataHelper = new AnnotationMetadataHelper(importingClassMetadata, getImportingAnnotationClass());
			annotationMetadataHelper.setResourceLoader(resourceLoader);
			annotationMetadataHelper.setClassLoader(classLoader);
			this.annotationMetadataHelper = annotationMetadataHelper;
		}
		return annotationMetadataHelper;
	}
	
	protected Stream<BeanDefinition> scanBeanDefinitions(AnnotationMetadata importingClassMetadata){
		return getAnnotationMetadataHelper(importingClassMetadata).scanBeanDefinitions(getApiClientTagAnnotationClass());
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Class<? extends Annotation> clientTagClass = getApiClientTagAnnotationClass();
		scanBeanDefinitions(importingClassMetadata)
								.filter(AnnotatedBeanDefinition.class::isInstance)
								.forEach(bd->{
									AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) bd;
									AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
									Assert.isTrue(annotationMetadata.isInterface(),
											"@"+clientTagClass.getSimpleName()+" can only be specified on an interface");

									AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(annotationMetadata, clientTagClass);
									registerApiClient(registry, annotationMetadata, attributes);
								});;
	}
	

	/***
	 * 
	 * @author wayshall
	 * @return
	 */
	abstract protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes);

	protected void registerApiClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		if(logger.isInfoEnabled()){
			logger.info("register api client: {}", className);
		}
		String name = resolveName(attributes, className);
		
		BeanDefinitionBuilder definition = createApiClientFactoryBeanBuilder(annotationMetadata, attributes);
		
		String alias = name + getApiClientTagAnnotationClass().getSimpleName();
		AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
		beanDefinition.setPrimary(true);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, new String[] { alias });
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}
	

	final protected String resolveName(AnnotationAttributes attributes, String defName) {
		String name = attributes.getString(ATTRS_NAME);
		if (!StringUtils.hasText(name)) {
			name = defName;
		}
		name = resolve(name);
		return name;
	}
	
	final protected String resolveUrl() {
		String url = resolve(annotationMetadataHelper.getAttributes().getString(ATTRS_BASE_URL));
		if (StringUtils.hasText(url)) {
			if (!url.contains("://")) {
				url = "http://" + url;
			}
			try {
				new URL(url);
			}
			catch (MalformedURLException e) {
				throw new IllegalArgumentException(url + " is malformed", e);
			}
		}
		return url;
	}

	final protected String resolvePath(AnnotationAttributes attributes) {
		String path = resolve(attributes.getString(ATTRS_PATH));
		if (StringUtils.hasText(path)) {
			path = path.trim();
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}
		}
		return path;
	}

	final protected String resolve(String value) {
		return SpringUtils.resolvePlaceholders(resourceLoader, value);
	}
}
