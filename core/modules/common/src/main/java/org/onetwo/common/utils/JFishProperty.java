package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public interface JFishProperty {

	public void setValue(Object entity, Object value);

	public Class<?> getBeanClass();
	public ClassWrapper<?> getBeanClassWrapper();

	public String getName();

	public boolean isTransientModifier();
	
	public int getModifiers();

	public Object getValue(Object entity);

	public Class<?> getType();
	
	public ClassWrapper<?> getTypeClassWrapper();
	
//	public void addAnnotations(Annotation...annotations);
	
	public boolean hasAnnotation(Class<? extends Annotation> annoClass);
	
	public <T extends Annotation> T getAnnotation(Class<T> annoClass);
	
	public AnnotationInfo getAnnotationInfo();

	public PropertyDescriptor getPropertyDescriptor();
	public Field getField();
	public Type getGenericType();
	public Type[] getParameterTypes();
	public Type getFirstParameterType();
	public ClassWrapper<?> getFirstParameterTypeClassWrapper();

	public boolean isCollectionType();
	public boolean isMapType();

}