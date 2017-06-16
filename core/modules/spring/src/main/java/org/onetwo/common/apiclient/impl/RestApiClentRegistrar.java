package org.onetwo.common.apiclient.impl;

import java.lang.annotation.Annotation;

import org.onetwo.common.apiclient.AbstractApiClentRegistrar;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class RestApiClentRegistrar extends AbstractApiClentRegistrar {

	@Override
	protected Class<? extends Annotation> getImportingAnnotationClass() {
		return EnableRestApiClient.class;
	}

	@Override
	protected Class<? extends Annotation> getApiClientTagAnnotationClass() {
		return RestApiClient.class;
	}

	@Override
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DefaultApiClientFactoryBean.class);

		definition.addPropertyValue("url", resolveUrl());
		definition.addPropertyValue("path", resolvePath(attributes));
//		definition.addPropertyValue("name", name);
		definition.addPropertyValue("interfaceClass", className);
//		definition.addPropertyValue("decode404", attributes.get("decode404"));
//		definition.addPropertyValue("fallback", attributes.get("fallback"));
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}

}