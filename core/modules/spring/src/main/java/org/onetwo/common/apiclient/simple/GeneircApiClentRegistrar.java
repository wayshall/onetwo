package org.onetwo.common.apiclient.simple;

import java.lang.annotation.Annotation;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.apiclient.impl.AbstractApiClentRegistrar;
import org.onetwo.common.apiclient.impl.DefaultApiClientFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class GeneircApiClentRegistrar<IMPORT extends Annotation, COMPONENT extends Annotation> extends AbstractApiClentRegistrar<IMPORT, COMPONENT> {

	private ApiClientResponseHandler<ApiClientMethod> responseHandler;
	
	@Override
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DefaultApiClientFactoryBean.class);

		definition.addPropertyValue("url", resolveUrl(attributes, annotationMetadata));
		definition.addPropertyValue("path", resolvePath(attributes));
		definition.addPropertyValue("interfaceClass", className);
		definition.addPropertyValue("responseHandler", responseHandler);
		definition.addPropertyReference("restExecutor", RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}

	final public void setResponseHandler(ApiClientResponseHandler<ApiClientMethod> responseHandler) {
		this.responseHandler = responseHandler;
	}
	
}
