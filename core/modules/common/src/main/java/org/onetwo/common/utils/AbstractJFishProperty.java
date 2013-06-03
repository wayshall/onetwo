package org.onetwo.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


abstract public class AbstractJFishProperty implements JFishProperty {

	protected final ClassWrapper<?> beanClassWrapper;
	protected AnnotationInfo annotationInfo;
	protected ClassWrapper<?> propertyClassWrapper;
	private final String name;
	
	
	public AbstractJFishProperty(String name, ClassWrapper<?> beanClassWrapper) {
		this.name = name;
		this.beanClassWrapper = beanClassWrapper;
	}
	
	public AnnotationInfo getAnnotationInfo() {
		return annotationInfo;
	}
	public ClassWrapper<?> getBeanClassWrapper() {
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

	public ClassWrapper<?> getFirstParameterTypeClassWrapper() {
		return ClassWrapper.wrap((Class)getFirstParameterType());
	}

	public ClassWrapper<?> getTypeClassWrapper() {
		return propertyClassWrapper;
	}

	public boolean isCollectionType(){
		return propertyClassWrapper.isCollectionType();
	}

	public boolean isMapType(){
		return propertyClassWrapper.isMapType();
	}
	
}
