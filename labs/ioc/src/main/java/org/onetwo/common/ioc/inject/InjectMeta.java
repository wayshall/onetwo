package org.onetwo.common.ioc.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.onetwo.common.ioc.ObjectInfo;

@SuppressWarnings("unchecked")
public interface InjectMeta {

	public Class getTargetClass();
	
	public Type getInjectType();

	public String getName();

	public void inject(Object bean, ObjectInfo injectValue);
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass);

}