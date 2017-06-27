package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.AbstractApiClentRegistrar;
import org.onetwo.common.apiclient.annotation.EnableRestApiClient;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class RestApiClentRegistrar extends AbstractApiClentRegistrar<EnableRestApiClient, RestApiClient> {

	@Override
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DefaultApiClientFactoryBean.class);

		definition.addPropertyValue("url", resolveUrl(attributes));
		definition.addPropertyValue("path", resolvePath(attributes));
//		definition.addPropertyValue("name", name);
		definition.addPropertyValue("interfaceClass", className);
//		definition.addPropertyValue("decode404", attributes.get("decode404"));
//		definition.addPropertyValue("fallback", attributes.get("fallback"));
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}

}
