package org.onetwo.common.spring.copier;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class CommonPropertyValueSetter implements PropertyValueCopier {
	
	public CommonPropertyValueSetter() {
	}
	
	@SuppressWarnings("unchecked")
	public void copyPropertyValue(SimpleBeanCopier beanCopier, BeanWrapper targetBeanWrapper, PropertyDescriptor toProperty, Object srcValue){
		String propertyName = toProperty.getName();
		if(srcValue==null){
			setPropertyValue0(targetBeanWrapper, propertyName, null);
			return ;
		}

//		Object targetValue = null;
		Type type = toProperty.getPropertyType();
		Class<?> propertyType = (Class<?>) type;
		Cloneable cloneable = getCloneableAnnotation(targetBeanWrapper.getWrappedInstance(), toProperty);
		if(isCopyValueOrRef(toProperty, cloneable)){
			setPropertyValue0(targetBeanWrapper, propertyName, srcValue);
			
		}else if(propertyType.isArray()){
			copyArray(beanCopier, targetBeanWrapper, propertyType, cloneable, toProperty, srcValue);
			
		}else if(Collection.class.isAssignableFrom(propertyType)){
			copyCollection(beanCopier, targetBeanWrapper, (Class<? extends Collection<?>>)propertyType, cloneable, toProperty, (Collection<?>)srcValue);
			
		}else if(Map.class.isAssignableFrom(propertyType)){
			copyMap(beanCopier, targetBeanWrapper, (Class<? extends Map<Object, Object>>)propertyType, cloneable, toProperty, (Map<?, ?>)srcValue);
			
		}else{
//			Object targetValue = newBeanCopier(toProperty.getPropertyType()).fromObject(srcValue);
			Object targetValue = beanCopier.fromObject(srcValue, toProperty.getPropertyType());
//			targetBeanWrapper.setPropertyValue(propertyName, targetValue);
			setPropertyValue0(targetBeanWrapper, propertyName, targetValue);
		}
	}

	protected void setPropertyValue0(BeanWrapper targetBeanWrapper, String propertyName, Object value) {
		targetBeanWrapper.setPropertyValue(propertyName, value);
	}
	
	/****
	 * 是否浅拷贝，只拷贝值或引用
	 * @param property
	 * @param cloneable
	 * @return
	 */
	protected boolean isCopyValueOrRef(PropertyDescriptor property, Cloneable cloneable){
//		return cloneable==null || isSimpleType(property.getPropertyType());
		return isValueTypes(property.getPropertyType()) || cloneable==null;
	}
	
	protected boolean isValueTypes(Object val){
		Class<?> cls = Class.class.isInstance(val)?(Class<?>)val:val.getClass();
		return LangUtils.getSimpleClass().contains(cls);
	}
	/****
	 * 是否浅拷贝
	 * @param cloneable
	 * @return
	 */
	protected boolean isContainerKeyCopyValueOrRef(Cloneable cloneable, Object key){
		return (key!=null && isValueTypes(key)) || cloneable==null || !cloneable.keyCloneable();
	}
	
	/****
	 * 是否浅拷贝
	 * @param cloneable
	 * @return
	 */
	protected boolean isContainerValueCopyValueOrRef(Cloneable cloneable, Object value){
		return (value!=null && isValueTypes(value)) || cloneable==null || !cloneable.valueCloneable();
	}
	
	protected Cloneable getCloneableAnnotation(Object target, PropertyDescriptor property){
		Cloneable cloneable = property.getReadMethod().getAnnotation(Cloneable.class);
		if(cloneable==null){
			Field field = ReflectionUtils.findField(target.getClass(), property.getName());
			if(field!=null){
				cloneable = field.getAnnotation(Cloneable.class);
			}
		}
		return cloneable;
	}

	protected void copyArray(SimpleBeanCopier beanCopier, BeanWrapper targetBeanWrapper, Class<?> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Object srcValue){
		Assert.isTrue(propertyType==srcValue.getClass());
		int length = Array.getLength(srcValue);
		Object array = Array.newInstance(propertyType.getComponentType(), length);
		
		if(isContainerValueCopyValueOrRef(cloneable, srcValue)){
			for (int i = 0; i < length; i++) {
				Array.set(array, i, Array.get(srcValue, i));
			}
		}else{
			for (int i = 0; i < length; i++) {
//				Object targetElement = newBeanCopier(propertyType.getComponentType()).fromObject(Array.get(array, i));
				Object targetElement = beanCopier.fromObject(Array.get(array, i), propertyType.getComponentType());
				Array.set(array, i, targetElement);
			}
		}
//		targetBeanWrapper.setPropertyValue(toProperty.getName(), array);
		setPropertyValue0(targetBeanWrapper, toProperty.getName(), array);
	}
	

	protected void copyCollection(SimpleBeanCopier beanCopier, BeanWrapper targetBeanWrapper, Class<? extends Collection<?>> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Collection<?> srcValue){
		Collection<Object> cols = CopyUtils.newCollections((Class<? extends Collection<?>>)propertyType);
		
		if(isContainerValueCopyValueOrRef(cloneable, srcValue)){
			cols.addAll(srcValue);
		}else{
			if(toProperty.getReadMethod().getGenericReturnType() instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType)toProperty.getReadMethod().getGenericReturnType();
				Type elementType = ptype.getActualTypeArguments()[0];
				
				for(Object element : srcValue){
//					Object targetElement = newBeanCopier((Class<?>)elementType).fromObject(element);
					Object targetElement = beanCopier.fromObject(element, (Class<?>)elementType);
					cols.add(targetElement);
				}
			}else{
				for(Object element : srcValue){
//					Object targetElement = newBeanCopier(element.getClass()).fromObject(element);
					Object targetElement = beanCopier.fromObject(element, element.getClass());
					cols.add(targetElement);
				}
			}
		}
//		ReflectionUtils.invokeMethod(toProperty.getWriteMethod(), target, cols);
//		targetBeanWrapper.setPropertyValue(toProperty.getName(), cols);
		setPropertyValue0(targetBeanWrapper, toProperty.getName(), cols);
	}
	
	protected void copyMap(SimpleBeanCopier beanCopier, BeanWrapper targetBeanWrapper, Class<? extends Map<Object, Object>> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Map<?, ?> srcValue){
		Map<Object, Object> map = null;
		if(isContainerKeyCopyValueOrRef(cloneable, null) && isContainerValueCopyValueOrRef(cloneable, null)){
			map = CopyUtils.newMap(propertyType);
			map.putAll(srcValue);
		}else{
			//for declare type is diff
			if(toProperty.getReadMethod().getGenericReturnType() instanceof ParameterizedType){
				
				ParameterizedType ptype = (ParameterizedType)toProperty.getReadMethod().getGenericReturnType();
				Class<?> keyType = (Class<?>)ptype.getActualTypeArguments()[0];
				Class<?> valueType = (Class<?>)ptype.getActualTypeArguments()[1];

				Object key = null;
				Object value = null;
				map = CopyUtils.newMap(propertyType);
				boolean keyTypeIsInterface = keyType.isInterface();
				boolean valueTypeIsInterface = valueType.isInterface();
				for(Map.Entry<?, ?> entry : srcValue.entrySet()){
					if(isContainerKeyCopyValueOrRef(cloneable, entry.getKey())){
						key = entry.getKey();
					}else{
//						key = newBeanCopier((Class<?>)keyType).fromObject(entry.getKey());
						key = beanCopier.fromObject(entry.getKey(), keyTypeIsInterface?entry.getKey().getClass():keyType);
					}
					if(isContainerValueCopyValueOrRef(cloneable, entry.getValue())){
						value = entry.getValue();
					}else{
//						value = newBeanCopier((Class<?>)valueType).fromObject(entry.getValue());
						value = beanCopier.fromObject(entry.getValue(), valueTypeIsInterface?entry.getValue().getClass():valueType);
					}
					map.put(key, value);
				}
			}else{
				map = copyMapWithSource(beanCopier, propertyType, cloneable, srcValue);
			}
			
		}
		
//		ReflectionUtils.invokeMethod(toProperty.getWriteMethod(), target, map);
//		targetBeanWrapper.setPropertyValue(toProperty.getName(), map);
		setPropertyValue0(targetBeanWrapper, toProperty.getName(), map);
	}
	
	protected Map<Object, Object> copyMapWithSource(SimpleBeanCopier beanCopier, Class<? extends Map<Object, Object>> propertyType, Cloneable cloneable, Map<?, ?> srcValue){
		//直接使用源对象的类型来复制  from 4.3.9
		Map<Object, Object> map = CopyUtils.newMap(propertyType);
		Object key = null;
		Object value = null;
		for(Map.Entry<?, ?> entry : srcValue.entrySet()){
			if(isContainerKeyCopyValueOrRef(cloneable, entry.getKey())){
				key = entry.getKey();
			}else{
//				key = newBeanCopier(entry.getKey().getClass()).fromObject(entry.getKey());
				key = beanCopier.fromObject(entry.getKey(), entry.getKey().getClass());
			}
			if(isContainerValueCopyValueOrRef(cloneable, entry.getValue())){
				value = entry.getValue();
			}else{
//				value = newBeanCopier(entry.getValue().getClass()).fromObject(entry.getValue());
				value = beanCopier.fromObject(entry.getValue(), entry.getValue().getClass());
			}
			map.put(key, value);
		}
		return map;
	}

}
