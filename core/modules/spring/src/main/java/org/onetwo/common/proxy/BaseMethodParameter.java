package org.onetwo.common.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import org.onetwo.common.reflect.ReflectUtils;
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
		Assert.notNull(annotationClass, "annotationClass can not be null");
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
		if(StringUtils.isBlank(name)){
			name = obtainParameterName();
			this.setParameterName(name);
		}
		return name;
	}
	
	protected String obtainParameterName(){
		String pname = null;
		if(this.parameter.isNamePresent()){
			return this.parameter.getName();
		}else{
			pname = String.valueOf(getParameterIndex());
		}
		return pname;
	}
	

	public <A extends Annotation> Optional<A> getOptionalParameterAnnotation(Class<A> annotationType) {
		return Optional.ofNullable(super.getParameterAnnotation(annotationType));
	}
	
	public void setParameterName(String parameterName){
		ReflectUtils.setFieldValue(this, "parameterName", parameterName);
	}
}
