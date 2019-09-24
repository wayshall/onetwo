package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.reflect.Intro;

public interface JFishProperty {
	/***
	 * 是否java bean属性，如果是property则返回是，如果是field则返回false	
	 * @author weishao zeng
	 * @return
	 */
	boolean isBeanProperty();

	void setValue(Object entity, Object value);

	Class<?> getBeanClass();
	Intro<?> getBeanClassWrapper();

	String getName();

	boolean isTransientModifier();
	
	int getModifiers();

	Object getValue(Object entity);

	Class<?> getType();
	
	Intro<?> getTypeClassWrapper();
	
//	void addAnnotations(Annotation...annotations);
	
	boolean hasAnnotation(Class<? extends Annotation> annoClass);
	
	<T extends Annotation> T getAnnotation(Class<T> annoClass);
//	<T extends Annotation> Set<T> getAnnotations(Class<T> annoClass);
	
	AnnotationInfo getAnnotationInfo();

	PropertyDescriptor getPropertyDescriptor();
	Field getField();
	Type getGenericType();
	Type[] getParameterTypes();
	Type getParameterType(int index);
	Type getFirstParameterType();
	Intro<?> getFirstParameterTypeClassWrapper();

	boolean isCollectionType();
	boolean isMapType();
	
	/****
	 * field对应的property，或property对应的field
	 * @return
	 */
	Optional<JFishProperty> getCorrespondingJFishProperty();

}