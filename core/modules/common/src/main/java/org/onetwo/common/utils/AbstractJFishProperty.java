package org.onetwo.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.reflect.Intro;


abstract public class AbstractJFishProperty implements JFishProperty {

	protected final Intro<?> beanClassWrapper;
	protected AnnotationInfo annotationInfo;
	protected Intro<?> propertyClassWrapper;
	private final String name;
	
	
	public AbstractJFishProperty(String name, Intro<?> beanClassWrapper) {
		this.name = name;
		this.beanClassWrapper = beanClassWrapper;
	}
	
	public AnnotationInfo getAnnotationInfo() {
		return annotationInfo;
	}
	public Intro<?> getBeanClassWrapper() {
		return beanClassWrapper;
	}

	public String getName() {
		return name;
	}

	public Type getGenericType() {
		Field f = getField();
		return f.getGenericType();
	}
	
	public Type[] getParameterTypes() {
		Type gtype = getGenericType();
		if(!ParameterizedType.class.isInstance(gtype))
			return null;
		ParameterizedType ptype = (ParameterizedType) gtype;
		Type[] types = ptype.getActualTypeArguments();
		return types;
	}
	
	public Type getFirstParameterType() {
		Type[] types = getParameterTypes();
		return LangUtils.isEmpty(types)?null:types[0];
	}

	public Intro<?> getFirstParameterTypeClassWrapper() {
		return Intro.wrap((Class<?>)getFirstParameterType());
	}

	public Intro<?> getTypeClassWrapper() {
		return propertyClassWrapper;
	}

	public boolean isCollectionType(){
		return propertyClassWrapper!=null && propertyClassWrapper.isCollectionType();
	}

	public boolean isMapType(){
		return propertyClassWrapper!=null && propertyClassWrapper.isMapType();
	}
	
}
