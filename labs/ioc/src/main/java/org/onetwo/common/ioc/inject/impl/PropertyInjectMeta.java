package org.onetwo.common.ioc.inject.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.inject.AbstractInjectMeta;

@SuppressWarnings("unchecked")
public class PropertyInjectMeta extends AbstractInjectMeta {

	private PropertyDescriptor property;
	private Method writeMethod;
	
	public PropertyInjectMeta(Class targetClass, PropertyDescriptor property){
		super(targetClass);
		this.property = property;
		this.writeMethod = property.getWriteMethod();
	}
	
	public Type getInjectType(){
		return property.getPropertyType();
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass){
		return writeMethod.getAnnotation(annotationClass);
	}
	
	public String getName(){
		return property.getName();
	}
	
	public void inject(Object bean, ObjectInfo objInfo){
		if(objInfo==null)
			return ;
		try {
			Object injectValue = getInjectValue(bean, objInfo);
			writeMethod.invoke(bean, injectValue);
		} catch (Exception e) {
			throw new ServiceException("inject error: name=" + getName(), ", method:"+this.writeMethod.toGenericString());
		} 
	}

}
