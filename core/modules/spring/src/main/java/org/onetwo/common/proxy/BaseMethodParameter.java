package org.onetwo.common.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.onetwo.common.utils.StringUtils;
import org.springframework.core.MethodParameter;

public class BaseMethodParameter extends MethodParameter {
	protected final Parameter parameter;
	

	public BaseMethodParameter(Method method, Parameter parameter, int parameterIndex) {
		super(method, parameterIndex);
		this.parameter = parameter;
	}
	public BaseMethodParameter(Method method, int parameterIndex) {
		super(method, parameterIndex);
		this.parameter = method.getParameters()[parameterIndex];
	}

	public Parameter getParameter() {
		return parameter;
	}

	@Override
	public String getParameterName() {
		String name = super.getParameterName();
		if(StringUtils.isBlank(name) && this.parameter.isNamePresent()){
			name = this.parameter.getName();
		}
		return name;
	}
}
