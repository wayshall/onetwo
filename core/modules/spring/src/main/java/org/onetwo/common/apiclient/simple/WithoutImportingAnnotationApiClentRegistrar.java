package org.onetwo.common.apiclient.simple;

import org.onetwo.common.apiclient.annotation.EnableRestApiClient;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.spring.context.AnnotationMetadataHelper;
import org.onetwo.common.spring.context.AnnotationMetadataHelper.NoAnnotationMetadataHelper;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Sets;

/**
 * 没有@EnableXxx注解的api注册器
 * @author weishao zeng
 * <br/>
 */
public class WithoutImportingAnnotationApiClentRegistrar extends SimpleApiClentRegistrar<EnableRestApiClient, RestApiClient> {

	private Class<?> componentBaseClass;
	
	public WithoutImportingAnnotationApiClentRegistrar(Class<?> componentBaseClass) {
		this.componentBaseClass = componentBaseClass;
	}

	protected AnnotationMetadataHelper createAnnotationMetadataHelper(AnnotationMetadata importingClassMetadata) {
		AnnotationMetadataHelper annotationMetadataHelper = new NoAnnotationMetadataHelper(
																		Sets.newHashSet(ClassUtils.getPackageName(componentBaseClass)), 
																		getImportingAnnotationClass()
																);
		annotationMetadataHelper.setResourceLoader(resourceLoader);
		annotationMetadataHelper.setClassLoader(classLoader);
		return annotationMetadataHelper;
	}
}
