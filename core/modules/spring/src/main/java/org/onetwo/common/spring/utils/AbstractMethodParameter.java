package org.onetwo.common.spring.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.springframework.core.MethodParameter;

public class AbstractMethodParameter extends MethodParameter {
	protected final Parameter parameter;
	
	public AbstractMethodParameter(Method method, int parameterIndex) {
		super(method, parameterIndex);
		this.parameter = method.getParameters()[parameterIndex];
	}

	public Parameter getParameter() {
		return parameter;
	}
}
