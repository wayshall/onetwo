package org.onetwo.common.apiclient.api.simple2;

import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.apiclient.impl.DefaultApiClientFactoryBean;
import org.onetwo.common.apiclient.simple.GenericApiClentRegistrar;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author weishao zeng
 * <br/>
 */
public class Simple2RestApiClentRegistrar extends GenericApiClentRegistrar<EnableSimpleApiClient, RestApiClient> {

	@Override
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DefaultApiClientFactoryBean.class);

		definition.addPropertyValue("url", resolveUrl(attributes));
		definition.addPropertyValue("path", resolvePath(attributes));
		definition.addPropertyValue("interfaceClass", className);
		definition.addPropertyReference("restExecutor", RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}

}

