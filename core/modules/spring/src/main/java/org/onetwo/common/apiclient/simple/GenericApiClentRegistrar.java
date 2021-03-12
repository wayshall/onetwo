package org.onetwo.common.apiclient.simple;

import java.lang.annotation.Annotation;

import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.ApiClientResponseHandler;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.apiclient.impl.AbstractApiClentRegistrar;
import org.onetwo.common.apiclient.impl.DefaultApiClientFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import net.jodah.typetools.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
public class GenericApiClentRegistrar<EnableApiClient, ApiClient> extends AbstractApiClentRegistrar {

	@SuppressWarnings("unchecked")
	public static <S extends GenericApiClentRegistrar<?, ?>> Class<? extends Annotation>[] resolveRawArguments(Class<S> subType) {
		Class<? extends Annotation>[] paramClasses = (Class<?  extends Annotation>[])TypeResolver.resolveRawArguments(GenericApiClentRegistrar.class, subType);
		return paramClasses;
	}

	private ApiClientResponseHandler<ApiClientMethod> responseHandler;

	protected GenericApiClentRegistrar() {
		Class<? extends Annotation>[] paramClasses = (Class<?  extends Annotation>[])resolveRawArguments(getClass());
//		this.importingAnnotationClass = paramClasses[0];
//		this.componentAnnotationClass = paramClasses[1];
		setImportingAnnotationClass(paramClasses[0]);
		setComponentAnnotationClass(paramClasses[1]);
	}
	
	public GenericApiClentRegistrar(Class<? extends Annotation> importingAnnotationClass,
			Class<? extends Annotation> componentAnnotationClass) {
		super(importingAnnotationClass, componentAnnotationClass);
	}
	
	@Override
	protected BeanDefinitionBuilder createApiClientFactoryBeanBuilder(AnnotationMetadata annotationMetadata, AnnotationAttributes attributes) {
		String className = annotationMetadata.getClassName();
		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(DefaultApiClientFactoryBean.class);

		definition.addPropertyValue("url", resolveUrl(attributes, annotationMetadata));
		definition.addPropertyValue("path", resolvePath(attributes));
		definition.addPropertyValue("interfaceClass", className);
		definition.addPropertyValue("responseHandler", responseHandler);
		definition.addPropertyReference("restExecutor", RestExecutorFactory.REST_EXECUTOR_FACTORY_BEAN_NAME);
//		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		
		return definition;
	}

	final public void setResponseHandler(ApiClientResponseHandler<ApiClientMethod> responseHandler) {
		this.responseHandler = responseHandler;
	}
	
}
