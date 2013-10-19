package org.onetwo.common.utils;


import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
		T annotation = clazz.getAnnotation(annotationClass);
		return annotation;
	}

	public static <T extends Annotation> T findAnnotation(Method method, Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
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
	
	public static <T extends Annotation> T findAnnotationWithStopClass(Class<?> clazz, Method method, Class<T> annotationClass, Class<?>...stopClass) {
		T annotation = null;
		if (method != null)
			annotation = method.getAnnotation(annotationClass);
		if (annotation == null)
			annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null) {
			Class<?> parent = clazz.getSuperclass();
			if (parent == null || ArrayUtils.contains(stopClass, parent))
				return null;

			Method parentMethod = null;
			if (method != null)
				parentMethod = findMethod(parent, method.getName(), method.getParameterTypes());
			return findAnnotationWithStopClass(parent, parentMethod, annotationClass, stopClass);
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
		T annotation = null;
		if (field != null)
			annotation = field.getAnnotation(annotationClass);
		return annotation;
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
