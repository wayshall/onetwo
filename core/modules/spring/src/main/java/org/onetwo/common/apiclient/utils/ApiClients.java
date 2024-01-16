package org.onetwo.common.apiclient.utils;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.annotation.AnnotationUtils;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.apiclient.impl.DefaultApiClientFactoryBean;
import org.onetwo.common.apiclient.impl.DefaultRestExecutorFactory;
import org.onetwo.common.apiclient.impl.RestExecutorConfig;
import org.onetwo.common.apiclient.utils.ApiClientConstants.ApiClientErrors;
import org.onetwo.common.exception.ApiClientException;
import org.onetwo.common.spring.rest.ExtRestTemplate;

/**
 * @author wayshall
 * <br/>
 */
abstract public class ApiClients {
	
	private static final DefaultRestExecutorFactory RestExecutorFactory = new DefaultRestExecutorFactory();
	
	
	@SuppressWarnings("unchecked")
	public static <T> T newClient(Class<T> clazz) {
		RestApiClient restApiClient = AnnotationUtils.findAnnotation(clazz, RestApiClient.class);
		DefaultApiClientFactoryBean fb = new DefaultApiClientFactoryBean();
		fb.setInterfaceClass(clazz);
		fb.setRestExecutor(inst());
		if (StringUtils.isNotBlank(restApiClient.url())) {
			fb.setUrl(restApiClient.url());
		}
		if (StringUtils.isNotBlank(restApiClient.path())) {
			fb.setPath(restApiClient.path());
		}
		try {
			fb.afterPropertiesSet();
			return (T)fb.getObject();
		} catch (Exception e) {
			throw new ApiClientException(ApiClientErrors.CREATE_CLIENT_INST_ERROR, e);
		}
	}
	
	public static ExtRestTemplate inst() {
		try {
			return (ExtRestTemplate)RestExecutorFactory.getObject();
		} catch (Exception e) {
			throw new ApiClientException(ApiClientErrors.CREATE_REST_ERROR, e);
		}
	}
	
	public static ExtRestTemplate inst(RestExecutorConfig restExecutorConfig) {
		DefaultRestExecutorFactory factory = new DefaultRestExecutorFactory();
		factory.setRestExecutorConfig(restExecutorConfig);
		try {
			return (ExtRestTemplate)factory.getObject();
		} catch (Exception e) {
			throw new ApiClientException(ApiClientErrors.CREATE_REST_ERROR, e);
		}
	}
	
	private ApiClients() {
	}

}
