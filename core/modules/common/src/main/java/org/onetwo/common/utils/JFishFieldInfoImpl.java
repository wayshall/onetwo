package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;

public class JFishFieldInfoImpl extends AbstractJFishProperty {

	private final Field field;
	private final String name;
	

	public JFishFieldInfoImpl(Class<?> beanClass, String fieldName){
		this(beanClass, ReflectUtils.findField(beanClass, fieldName));
	}
	
	public JFishFieldInfoImpl(Class<?> beanClass, Field field){
		this(Intro.wrap(beanClass), field);
	}

	public JFishFieldInfoImpl(Intro<?> beanClassWrapper, Field field){
		super(field.getName(), beanClassWrapper);
		this.name = field.getName();
		if(!field.isAccessible()){
			field.setAccessible(true);
		}
		this.field = field;
		this.propertyClassWrapper = Intro.wrap(field.getType());
		this.annotationInfo = new AnnotationInfo(beanClassWrapper.getClazz(), field.getAnnotations());
	}

	@Override
	public void setValue(Object entity, Object value){
		Assert.notNull(entity);
		try {
			field.set(entity, value);
		} catch (Exception e) {
			throw new BaseException("set bean ["+getBeanClass()+"] field["+getName()+"] fieldType["+this.getType()+"] value error", e);
		}
	}
	
	
	@Override
	public Class<?> getBeanClass() {
		return beanClassWrapper.getClazz();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getValue(Object entity){
		Assert.notNull(entity);
		try {
			return field.get(entity);
		} catch (Exception e) {
			throw new BaseException("get bean ["+getBeanClass()+"] field["+getName()+"] fieldType["+this.getType()+"] value error", e);
		}
	}
	
	@Override
	public Class<?> getType() {
		return field.getType();
	}

	@Override
	public boolean isTransientModifier() {
		return Modifier.isTransient(field.getModifiers());
	}

	@Override
	public int getModifiers() {
		return field.getModifiers();
	}
	
	@Override
	public Intro<?> getBeanClassWrapper() {
		return beanClassWrapper;
	}

	public String toString(){
		return getName();
	}

	@Override
	public PropertyDescriptor getPropertyDescriptor() {
		return beanClassWrapper.getProperty(name);
	}

	@Override
	public Field getField() {
		return field;
	}

	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annoClass) {
		return getAnnotationInfo().hasAnnotation(annoClass);
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annoClass) {
		return getAnnotationInfo().getAnnotation(annoClass);
	}

	
}
