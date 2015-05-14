package org.onetwo.plugins.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.onetwo.common.proxy.AbstractMethodResolver;
import org.onetwo.common.proxy.BaseMethodParameter;

public class RestMethodResolver extends AbstractMethodResolver<BaseMethodParameter> {

	public RestMethodResolver(Method method) {
		super(method);
	}

	@Override
	protected BaseMethodParameter createMethodParameter(Method method, int parameterIndex, Parameter parameter) {
		return new BaseMethodParameter(method, parameter, parameterIndex);
	}

	/*protected static class RestMethodParameter extends BaseMethodParameter {

		public RestMethodParameter(Method method, int parameterIndex) {
			super(method, parameterIndex);
		}
		
	}*/
}
