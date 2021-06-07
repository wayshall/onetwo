package org.onetwo.common.apiclient.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.apiclient.annotation.ApiClientInterceptor;
import org.onetwo.common.apiclient.interceptor.ApiInterceptor;
import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class BaseApiClientMethod<P extends BaseMethodParameter> extends AbstractMethodResolver<P> {

	private List<ApiInterceptor> interceptors;
	
	public BaseApiClientMethod(Method method) {
		super(method);
		this.initInterceptors();
	}

	/*@Override
	protected P createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
		return new BaseMethodParameter(method, parameter, parameterIndex);
	}*/

	final protected void initInterceptors() {
		ApiClientInterceptor interceptorAnno = findAnnotation(ApiClientInterceptor.class);
		if (interceptorAnno==null) {
			this.interceptors = Collections.emptyList();
		} else {
			this.interceptors = Stream.of(interceptorAnno.value()).map(cls -> {
				return (ApiInterceptor)createAndInitComponent(cls);
			})
			.collect(Collectors.toList());
		}
	}

	public List<ApiInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<ApiInterceptor> interceptors) {
		this.interceptors = interceptors;
	}
	
	public static class SimpleApiClientMethod extends BaseApiClientMethod<BaseMethodParameter> {

		public SimpleApiClientMethod(Method method) {
			super(method);
		}

		@Override
		protected BaseMethodParameter createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
			return new BaseMethodParameter(method, parameter, parameterIndex);
		}
		
	}
}

