package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class BeanCopier<T> {

	private static final List<Class<?>> BASE_CLASS;
	private static final List<Class<?>> SIMPLE_CLASS;

	static {
		
		List<Class<?>> cls = new ArrayList<Class<?>>();
		cls.add(Boolean.class);
		cls.add(boolean.class);
		cls.add(Character.class);
		cls.add(char.class);
		cls.add(Byte.class);
		cls.add(byte.class);
		cls.add(Short.class);
		cls.add(short.class);
		cls.add(Integer.class);
		cls.add(int.class);
		cls.add(Long.class);
		cls.add(long.class);
		cls.add(Float.class);
		cls.add(float.class);
		cls.add(Double.class);
		cls.add(double.class);
		cls.add(String.class);
		BASE_CLASS = Collections.unmodifiableList(cls);
		
		List<Class<?>> simples = new ArrayList<Class<?>>(cls);
//		simples.add(String.class);
		simples.add(Date.class);
		simples.add(Calendar.class);
		simples.add(Number.class);
		
		SIMPLE_CLASS = Collections.unmodifiableList(simples);
	}
	
	public static final <R> BeanCopier<R> newBeanCopier(Class<R> targetClass, PropertyNameConvertor propertyNameConvertor){
		BeanCopier<R> copier = new BeanCopier<R>(CopyUtils.newInstance(targetClass), propertyNameConvertor);
		return copier;
	}
	
	private BeanWrapper targetBeanWrapper;
	private PropertyNameConvertor propertyNameConvertor;
	private final T target;
	private boolean ignoreNull;
	

	public BeanCopier(T target) {
		this(target, null);
	}

	public BeanCopier(T target, PropertyNameConvertor convertor) {
		super();
		this.target = target;
		this.targetBeanWrapper = CopyUtils.newBeanWrapper(target);
		this.propertyNameConvertor = convertor;
	}
	
	protected boolean isSimpleType(Class<?> clazz){
		return SIMPLE_CLASS.contains(clazz);
	}
	
	/***
	 * deep copy
	 * @param src
	 * @return
	 */
	public T fromObject(Object src){
    	BeanWrapper srcBean = CopyUtils.newBeanWrapper(src);
    	PropertyDescriptor[] properties = targetBeanWrapper.getPropertyDescriptors();
		for(PropertyDescriptor property : properties){
			if(property.getWriteMethod()==null)
				continue ;
			Object srcValue = getPropertyValue(srcBean, property);
			setPropertyValue(property, srcValue);
    	}
		return target;
	}
	
	public Cloneable getCloneableAnnotation(PropertyDescriptor property){
		Cloneable cloneable = property.getReadMethod().getAnnotation(Cloneable.class);
		if(cloneable==null){
			Field field = ReflectionUtils.findField(target.getClass(), property.getName());
			if(field!=null){
				cloneable = field.getAnnotation(Cloneable.class);
			}
		}
		return cloneable;
	}

	private void setPropertyValue(PropertyDescriptor property, Object srcValue){
		String propertyName = property.getName();
		if(srcValue==null){
			if(!ignoreNull){
				targetBeanWrapper.setPropertyValue(propertyName, null);
			}
			return ;
		}

//		Object targetValue = null;
		Type type = property.getPropertyType();
		Class<?> propertyType = (Class<?>) type;
		if(isSimpleType(propertyType) || getCloneableAnnotation(property)==null){
			targetBeanWrapper.setPropertyValue(propertyName, srcValue);
			
		}else if(propertyType.isArray()){
			Assert.isTrue(propertyType==srcValue.getClass());
			int length = Array.getLength(srcValue);
			Object array = Array.newInstance(propertyType.getComponentType(), length);
			
			if(isSimpleType(propertyType.getComponentType())){
				for (int i = 0; i < length; i++) {
					Array.set(array, i, Array.get(srcValue, i));
				}
				targetBeanWrapper.setPropertyValue(propertyName, array);
			}else{
				for (int i = 0; i < length; i++) {
					Object targetElement = newBeanCopier(propertyType.getComponentType(), propertyNameConvertor).fromObject(Array.get(array, i));
					Array.set(array, i, targetElement);
				}
				targetBeanWrapper.setPropertyValue(propertyName, array);
			}
			
		}else if(Collection.class.isAssignableFrom(propertyType)){
			Collection<Object> cols = CopyUtils.newCollections((Class<? extends Collection>)propertyType);
			if(property.getReadMethod().getGenericReturnType() instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType)property.getReadMethod().getGenericReturnType();
				Type elementType = ptype.getActualTypeArguments()[0];
				for(Object element : (Collection<?>)srcValue){
					Object targetElement = newBeanCopier((Class<?>)elementType, propertyNameConvertor).fromObject(element);
					cols.add(targetElement);
				}
			}else{
				for(Object element : (Collection<?>)srcValue){
					Object targetElement = newBeanCopier(element.getClass(), propertyNameConvertor).fromObject(element);
					cols.add(targetElement);
				}
			}
			ReflectionUtils.invokeMethod(property.getWriteMethod(), target, cols);
		}else{
			Object targetValue = newBeanCopier(property.getPropertyType(), propertyNameConvertor).fromObject(srcValue);
			targetBeanWrapper.setPropertyValue(propertyName, targetValue);
		}
		
	}

	private Object getPropertyValue(BeanWrapper srcBean, PropertyDescriptor property){
		String targetPropertyName = property.getName();
		Object srcValue = null;
		if(propertyNameConvertor!=null){
			if(srcBean.isReadableProperty(targetPropertyName)){
    			srcValue = srcBean.getPropertyValue(targetPropertyName);
			}else{
    			srcValue = srcBean.getPropertyValue(propertyNameConvertor.convert(targetPropertyName));
			}
		}else{
			srcValue = srcBean.getPropertyValue(targetPropertyName);
		}
		return srcValue;
	}

	public BeanCopier<T> ignoreNull() {
		this.ignoreNull = true;
		return this;
	}
	
}
