package org.onetwo.common.apiclient.impl;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.context.AbstractImportRegistrar;
import org.onetwo.common.spring.context.AnnotationMetadataHelper;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractApiClentRegistrar extends AbstractImportRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, ResourceLoaderAware {

	public static final String ATTRS_URL = "url";
	public static final String ATTRS_BASE_URL = "baseUrl";
	public static final String ATTRS_NAME = "name";
	public static final String ATTRS_PATH = "path";
	public static final String ATTRS_REST_EXECUTOR_FACTORY = "restExecutorFactory";
	
//	private RestExecutor restExecutor;

	protected AbstractApiClentRegistrar() {
	}
	
	protected AbstractApiClentRegistrar(Class<? extends Annotation> importingAnnotationClass,
			Class<? extends Annotation> componentAnnotationClass) {
		super(importingAnnotationClass, componentAnnotationClass);
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//		this.regiseterRestExecutor(getAnnotationMetadataHelper(importingClassMetadata), registry);
		super.registerBeanDefinitions(importingClassMetadata, registry);
	}
	
	@Override
	protected void checkComponent(Class<? extends Annotation> componentAnnoClass, AnnotationMetadata annotationMetadata) {
		Assert.isTrue(annotationMetadata.isInterface(),
				"@"+componentAnnoClass.getSimpleName()+" can only be specified on an interface");
	}
	
	/***
	 * @see RestApiClientConfiguration#apiClientRestExecutor()
	 * @author wayshall
	 * @param annotationMetadataHelper
	 * @param registry
	 */
	@Deprecated
	protected void regiseterRestExecutor(AnnotationMetadataHelper annotationMetadataHelper, BeanDefinitionRegistry registry){
		if(registry.containsBeanDefinition(RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME)){
			return ;
		}
		Class<?> restExecutorFacotryClass = (Class<?>)annotationMetadataHelper.getAttributes().get(ATTRS_REST_EXECUTOR_FACTORY);
//		RestExecutorFactory factory = null;
		if(restExecutorFacotryClass==null || restExecutorFacotryClass==RestExecutorFactory.class){
			restExecutorFacotryClass = DefaultRestExecutorFactory.class;
		}
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(restExecutorFacotryClass);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		registry.registerBeanDefinition(RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME, definition.getBeanDefinition());
		
	}

	/***
	 * annotationMetadataHelper可拿到importingClassMetadata相关信息
	 * @author wayshall
	 * @return
	 */
	abstract protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes);
	
	/****
	 * 增加 importingClassMetadata 参数的子类方法，适配新的需求
	 * 有了此方法，另一个方法空实现即可
	 * @author weishao zeng
	 * @param importingClassMetadata
	 * @param annotationMetadata
	 * @param attributes
	 * @return
	 */
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata importingClassMetadata, AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		return createApiClientFactoryBeanBuilder(annotationMetadata, attributes);
	}


	@Override
	protected BeanDefinitionBuilder createComponentFactoryBeanBuilder(
			AnnotationMetadata importingClassMetadata,
			AnnotationMetadata annotationMetadata,
			AnnotationAttributes attributes) {
		return createApiClientFactoryBeanBuilder(importingClassMetadata, annotationMetadata, attributes);
	}


	final protected String resolveUrl(AnnotationAttributes tagAttributes) {
		return resolveUrl(tagAttributes, null);
	}
	
	final protected String resolveUrl(AnnotationAttributes tagAttributes, AnnotationMetadata annotationMetadata) {
		String url = resolve(tagAttributes.getString(ATTRS_URL));
		if (!StringUtils.hasText(url) && annotationMetadataHelper!=null){
			url = resolve(annotationMetadataHelper.getAttributes().getString(ATTRS_BASE_URL));
		}
		// 解释类上的RequestMapping作为base
		if (annotationMetadata!=null) {
			Map<String, Object> requestMapping = annotationMetadata.getAnnotationAttributes(RequestMapping.class.getName());
			if (requestMapping!=null) {
				String[] values = (String[])requestMapping.get("value");
				if (values!=null && values.length>0) {
					url = StringUtils.trimEndWith(url, FileUtils.SLASH) + StringUtils.appendStartWithSlash(resolve(values[0]));
				}
			}
		}
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

}
