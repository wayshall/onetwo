package org.onetwo.common.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.onetwo.common.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

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
	
	public AnnotationAttributes getAnnotationAttributes(Class<? extends Annotation> annotationClass){
		Assert.notNull(annotationClass);
		Annotation anno = getParameterAnnotation(annotationClass);
		return AnnotationUtils.getAnnotationAttributes(null, anno);
	}

	public Parameter getParameter() {
		return parameter;
	}

	public boolean isNamePresent() {
		return parameter.isNamePresent();
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
