package org.onetwo.common.annotation;


import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("unchecked")
public class AnnotationUtils {

	public static final Class<? extends Annotation>[] EMPTY_CLASSES = new Class[0];
	private AnnotationUtils() {
	}
	
	public static boolean containsAny(Annotation[] annos, Class<? extends Annotation>... annoClasses){
		if(LangUtils.isEmpty(annos))
			return false;
		for(Annotation anno : annos){
			if(ReflectUtils.isInstanceOfAny(anno, annoClasses))
				return true;
		}
		return false;
	}
	
	public static boolean isFieldContains(Class<?> target, String fieldName, Class<? extends Annotation>... annoClasses){
		return containsAny(ReflectUtils.getIntro(target).getField(fieldName).getAnnotations(), annoClasses);
	}

	public static Field findFirstField(Class<?> clazz, Class annotationClass) {
		List<Field> fields = findAnnotationField(clazz, annotationClass);
		if(fields==null || fields.isEmpty())
			return null;
		return fields.get(0);
	}

	public static List<Field> findAnnotationField(Class<?> clazz, Class annotationClass) {
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(clazz);
		if(fields==null || fields.isEmpty())
			return null;
		Annotation annotation = null;
		List<Field> annoFields = new ArrayList<Field>();
		for(Field f : fields){
			annotation = f.getAnnotation(annotationClass);
			if(annotation!=null)
				annoFields.add(f);
		}
		return annoFields;
	}

	public static List<PropertyDescriptor> findAnnotationProperties(Class<?> clazz, Class annotationClass) {
		PropertyDescriptor[] props = ReflectUtils.desribProperties(clazz);
		if(props==null || props.length==0)
			return null;
		Annotation annotation = null;
		List<PropertyDescriptor> annoFields = new ArrayList<PropertyDescriptor>();
		for(PropertyDescriptor p : props){
			annotation = ReflectUtils.getReadMethod(clazz, p).getAnnotation(annotationClass);
			if(annotation!=null)
				annoFields.add(p);
		}
		return annoFields;
	}

	public static PropertyDescriptor findAnnotationProperty(Class<?> clazz, Class annotationClass) {
		List<PropertyDescriptor> props = findAnnotationProperties(clazz, annotationClass);
		return LangUtils.hasElement(props)?props.get(0):null;
	}

	public static <T extends Annotation> T findAnnotationWithDeclaring(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz==null?null:clazz.getAnnotation(annotationClass);
		if(annotation!=null)
			return annotation;
		return (clazz==null || clazz.getDeclaringClass()==null)?null:findAnnotationWithDeclaring(clazz.getDeclaringClass(), annotationClass);
	}
	

	/****
	 * 查找注解，包括类的继承类和接口
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T findAnnotationWithParent(Class<?> clazz, Class<T> annotationClass) {
		T annotation = findAnnotationWithSupers(clazz, annotationClass);
		if(annotation!=null)
			annotation = findAnnotationWithInterfaces(clazz, annotationClass);
		return annotation;
	}

	public static <T extends Annotation> T findAnnotationWithSupers(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz==null?null:clazz.getAnnotation(annotationClass);
		if(annotation!=null)
			return annotation;
		return (clazz==null || clazz.getSuperclass()==Object.class)?null:findAnnotationWithSupers(clazz.getSuperclass(), annotationClass);
	}

	public static <T extends Annotation> T findAnnotationWithInterfaces(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz.getAnnotation(annotationClass);
		if(annotation!=null)
			return annotation;
//		Class<?>[] interfaces = clazz.getInterfaces();
		Set<Class<?>> superClasses = new LinkedHashSet<Class<?>>();
		if(clazz.getSuperclass()!=Object.class){
			superClasses.add(clazz.getSuperclass());
		}
		superClasses.addAll(Arrays.asList(clazz.getInterfaces()));
		for(Class<?> interf : superClasses){
			annotation = findAnnotationWithInterfaces(interf, annotationClass);
			if(annotation!=null)
				break;
		}
		return annotation;
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz.getAnnotation(annotationClass);
		return annotation;
	}

	public static <T extends Annotation> T findAnnotation(Method method, Class<T> annotationClass) {
		return findAnnotation(method, annotationClass, false);
	}
	public static <T extends Annotation> T findAnnotation(Method method, Class<T> annotationClass, boolean findInClass) {
		T annotation = method.getAnnotation(annotationClass);
		if(annotation==null && findInClass){
			annotation = method.getDeclaringClass().getAnnotation(annotationClass);
		}
		return annotation;
	}

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, String methodName, Class<T> annotationClass) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return findAnnotationWithStopClass(clazz, method, annotationClass);
	}

	public static final Class<?>[] DEFAULT_STOP_CLASS = new Class[]{Object.class};
	
	public static <T extends Annotation> T findMethodAnnotationWithStopClass(Method method, Class<T> annotationClass) {
		return findAnnotationWithStopClass(method.getDeclaringClass(), method, annotationClass, DEFAULT_STOP_CLASS);
	}
	
	public static <T extends Annotation> T findMethodAnnotationWithStopClass(Method method, Class<T> annotationClass, Class<?>...stopClass) {
		return findAnnotationWithStopClass(method.getDeclaringClass(), method, annotationClass, stopClass);
	}
	
	public static <T extends Annotation> T findAnnotationWithStopClass(Class<?> clazz, Method method, Class<T> annotationClass, Class<?>...stopClass) {
		return findAnnotationWithStopClass(true, clazz, method, annotationClass, stopClass);
	}
	public static <T extends Annotation> T findAnnotationWithStopClass(boolean findInClass, Class<?> clazz, Method method, Class<T> annotationClass, Class<?>...stopClass) {
		Objects.requireNonNull(method);
		if(LangUtils.isEmpty(stopClass)){
			stopClass = DEFAULT_STOP_CLASS;
		}
		T annotation = null;
		if (method != null)
			annotation = method.getAnnotation(annotationClass);
		if (annotation == null && findInClass)
			annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null) {
			Class<?> superClass = clazz.getSuperclass();
			Set<Class<?>> superClasses = new LinkedHashSet<Class<?>>();
			superClasses.add(superClass);
			Class<?>[] clsArray = clazz.getInterfaces();
			if(!LangUtils.isEmpty(clsArray)){
				Collections.addAll(superClasses, clsArray);
			}
			for(Class<?> parent : superClasses){
				if (parent == null || ArrayUtils.contains(stopClass, parent)){
					continue;
				}

				Method parentMethod = findMethod(parent, method.getName(), method.getParameterTypes());
				if(parentMethod==null){
//					throw new BaseException("parent method["+method.getName()+"] not found in class: " + parent);
					continue;
				}
				annotation = findAnnotationWithStopClass(findInClass, parent, parentMethod, annotationClass, stopClass);
				if(annotation!=null){
					return annotation;
				}
			}
		}
		return annotation;
	}
	

	public static <T extends Annotation> T findFieldAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
		try {
			Field field = ReflectUtils.findDeclaredField(clazz, fieldName);
			return findFieldAnnotation(clazz, field, annotationClass);
		} catch (Exception e) {
			return null;
		}
	}

	@Deprecated
	public static Field findField(Class<?> clazz, String fieldName) {
		return ReflectUtils.findDeclaredField(clazz, fieldName);
	}

	public static <T extends Annotation> T findFieldAnnotation(Class<?> clazz, Field field, Class<T> annotationClass) {
		return field != null?field.getAnnotation(annotationClass):null;
	}

	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return method;
	}
}
