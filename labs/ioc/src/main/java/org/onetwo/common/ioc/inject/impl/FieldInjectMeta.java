package org.onetwo.common.ioc.inject.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.inject.AbstractInjectMeta;
import org.onetwo.common.utils.ReflectUtils;

public class FieldInjectMeta extends AbstractInjectMeta {
	
	private Field field;
	
	public FieldInjectMeta(Field field){
		super(field.getDeclaringClass());
		this.field = field;
	}
	
	public Type getInjectType(){
		return field.getGenericType();
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass){
		return field.getAnnotation(annotationClass);
	}
	
	public String getName(){
		return field.getName();
	}
	
	public void inject(Object bean, ObjectInfo objectInfo){
		if(objectInfo==null)
			return ;
		Object injectValue = getInjectValue(bean, objectInfo);
		ReflectUtils.setFieldValue(field, bean, injectValue);
	}

}
