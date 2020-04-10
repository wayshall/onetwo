package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;

import org.onetwo.common.apiclient.ApiClientMethod;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultApiClientFactoryBean extends AbstractApiClientFactoryBean<ApiClientMethod> {

	final protected static Cache<Method, ApiClientMethod> API_METHOD_CACHES = CacheBuilder.newBuilder().build();
//																.build(new CacheLoader<Method, ApiClientMethod>() {
//																	@Override
//																	public ApiClientMethod load(Method method) throws Exception {
//																		ApiClientMethod apiMethod = new ApiClientMethod(method);
//																		apiMethod.initialize();
//																		return apiMethod;
//																	}
//																});

	protected DefaultApiMethodInterceptor createApiMethodInterceptor() {
		DefaultApiMethodInterceptor apiClient = new DefaultApiMethodInterceptor(API_METHOD_CACHES) {
			protected ApiClientMethod createMethod(Method method) {
				ApiClientMethod apiMethod = new ApiClientMethod(method);
				apiMethod.initialize();
				return apiMethod;
			}
		};
		return apiClient;
	}
	
	
}
