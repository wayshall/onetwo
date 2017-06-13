package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;

import org.onetwo.common.apiclient.AbstractApiClientFactoryBean;
import org.onetwo.common.apiclient.ApiClientMethod;
import org.onetwo.common.apiclient.AbstractApiClientFactoryBean.DefaultApiMethodInterceptor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultApiClientFactoryBean extends AbstractApiClientFactoryBean<ApiClientMethod> {

	final protected static LoadingCache<Method, ApiClientMethod> API_METHOD_CACHES = CacheBuilder.newBuilder()
																.build(new CacheLoader<Method, ApiClientMethod>() {
																	@Override
																	public ApiClientMethod load(Method method) throws Exception {
																		return new ApiClientMethod(method);
																	}
																});

	protected DefaultApiMethodInterceptor createApiMethodInterceptor() {
		DefaultApiMethodInterceptor apiClient = new DefaultApiMethodInterceptor(API_METHOD_CACHES);
		return apiClient;
	}
}
