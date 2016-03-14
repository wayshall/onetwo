package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class SimpleBeanCopier {
	
	public static SimpleBeanCopier unmodifyCopier(SimpleBeanCopier copier){
		return new SimpleBeanCopier(copier.propertyNameConvertor, copier.propertyFilter){

			@Override
			public void setPropertyFilter(PropertyFilter propertyFilter) {
				throw new UnsupportedOperationException("can not modify!");
			}
			@Override
			public void setPropertyNameConvertor(
					PropertyNameConvertor propertyNameConvertor) {
				throw new UnsupportedOperationException("can not modify!");
			}
	    	
	    };
	}
    
    public static final PropertyNameConvertor NOTHING_CONVERTOR = (name)-> name;

	
//	private BeanWrapper targetBeanWrapper;
	private PropertyNameConvertor propertyNameConvertor = NOTHING_CONVERTOR;
//	private final T target;
//	private boolean ignoreNull;
	private PropertyFilter propertyFilter = SimplePropertyFilters.IGNORE_NULL;
	

	public SimpleBeanCopier() {
	}

	public SimpleBeanCopier(PropertyNameConvertor convertor) {
		this.propertyNameConvertor = convertor;
	}

	public SimpleBeanCopier(PropertyNameConvertor propertyNameConvertor, PropertyFilter propertyFilter) {
		super();
		this.propertyNameConvertor = propertyNameConvertor;
		this.propertyFilter = propertyFilter;
	}

	public <T> T fromObject(Object src, Class<T> targetClass){
		return fromObject(src, ReflectUtils.newInstance(targetClass));
	}
	
	public <T> T fromObject(Object src, T target){
		BeanWrapper targetBeanWrapper = SpringUtils.newBeanWrapper(target);
		
    	BeanWrapper srcBean = CopyUtils.newBeanWrapper(src);
    	PropertyDescriptor[] properties = targetBeanWrapper.getPropertyDescriptors();
		for(PropertyDescriptor property : properties){
			/*if(property.getWriteMethod()==null)
				continue ;*/
			//if no writable
			if(!targetBeanWrapper.isWritableProperty(property.getName())){
				continue;
			}
			Object srcValue = getPropertyValue(srcBean, property);
			if(propertyFilter!=null && !propertyFilter.isCopiable(property, srcValue)){
				continue;
			}
			setPropertyValue(targetBeanWrapper, property, srcValue);
    	}
		return target;
	}

	public void setPropertyFilter(PropertyFilter propertyFilter) {
		this.propertyFilter = propertyFilter;
	}
	
	protected boolean isCopyValueOrRef(PropertyDescriptor property, Cloneable cloneable){
//		return cloneable==null || isSimpleType(property.getPropertyType());
		return cloneable==null;
	}
	
	protected boolean isContainerKeyCopyValueOrRef(Cloneable cloneable){
		return cloneable==null || !cloneable.keyCloneable();
	}
	
	protected boolean isContainerValueCopyValueOrRef(Cloneable cloneable){
		return cloneable==null || !cloneable.valueCloneable();
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

	protected void setPropertyValue(BeanWrapper targetBeanWrapper, PropertyDescriptor toProperty, Object srcValue){
		String propertyName = toProperty.getName();
		if(srcValue==null){
			targetBeanWrapper.setPropertyValue(propertyName, null);
			return ;
		}

//		Object targetValue = null;
		Type type = toProperty.getPropertyType();
		Class<?> propertyType = (Class<?>) type;
		Cloneable cloneable = this.getCloneableAnnotation(targetBeanWrapper.getWrappedInstance(), toProperty);
		if(isCopyValueOrRef(toProperty, cloneable)){
			targetBeanWrapper.setPropertyValue(propertyName, srcValue);
			
		}else if(propertyType.isArray()){
			this.copyArray(targetBeanWrapper, propertyType, cloneable, toProperty, srcValue);
			
		}else if(Collection.class.isAssignableFrom(propertyType)){
			this.copyCollection(targetBeanWrapper, (Class<? extends Collection<?>>)propertyType, cloneable, toProperty, (Collection<?>)srcValue);
			
		}else if(Map.class.isAssignableFrom(propertyType)){
			this.copyMap(targetBeanWrapper, (Class<? extends Map>)propertyType, cloneable, toProperty, (Map<?, ?>)srcValue);
			
		}else{
//			Object targetValue = newBeanCopier(toProperty.getPropertyType()).fromObject(srcValue);
			Object targetValue = fromObject(srcValue, toProperty.getPropertyType());
			targetBeanWrapper.setPropertyValue(propertyName, targetValue);
		}
		
	}
	

	protected void copyArray(BeanWrapper targetBeanWrapper, Class<?> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Object srcValue){
		Assert.isTrue(propertyType==srcValue.getClass());
		int length = Array.getLength(srcValue);
		Object array = Array.newInstance(propertyType.getComponentType(), length);
		
		if(isContainerValueCopyValueOrRef(cloneable)){
			for (int i = 0; i < length; i++) {
				Array.set(array, i, Array.get(srcValue, i));
			}
		}else{
			for (int i = 0; i < length; i++) {
//				Object targetElement = newBeanCopier(propertyType.getComponentType()).fromObject(Array.get(array, i));
				Object targetElement = fromObject(Array.get(array, i), propertyType.getComponentType());
				Array.set(array, i, targetElement);
			}
		}
		targetBeanWrapper.setPropertyValue(toProperty.getName(), array);
	}
	

	protected void copyCollection(BeanWrapper targetBeanWrapper, Class<? extends Collection<?>> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Collection<?> srcValue){
		Collection<Object> cols = CopyUtils.newCollections((Class<? extends Collection>)propertyType);
		
		if(isContainerValueCopyValueOrRef(cloneable)){
			cols.addAll(srcValue);
		}else{
			if(toProperty.getReadMethod().getGenericReturnType() instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType)toProperty.getReadMethod().getGenericReturnType();
				Type elementType = ptype.getActualTypeArguments()[0];
				
				for(Object element : srcValue){
//					Object targetElement = newBeanCopier((Class<?>)elementType).fromObject(element);
					Object targetElement = fromObject(element, (Class<?>)elementType);
					cols.add(targetElement);
				}
			}else{
				for(Object element : srcValue){
//					Object targetElement = newBeanCopier(element.getClass()).fromObject(element);
					Object targetElement = fromObject(element, element.getClass());
					cols.add(targetElement);
				}
			}
		}
//		ReflectionUtils.invokeMethod(toProperty.getWriteMethod(), target, cols);
		targetBeanWrapper.setPropertyValue(toProperty.getName(), cols);
	}
	
	protected void copyMap(BeanWrapper targetBeanWrapper, Class<? extends Map> propertyType, Cloneable cloneable, PropertyDescriptor toProperty, Map<?, ?> srcValue){
		Map<Object, Object> map = CopyUtils.newMap(propertyType);
		if(isContainerKeyCopyValueOrRef(cloneable) && isContainerValueCopyValueOrRef(cloneable)){
			map.putAll(srcValue);
		}else{
			if(toProperty.getReadMethod().getGenericReturnType() instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType)toProperty.getReadMethod().getGenericReturnType();
				Type keyType = ptype.getActualTypeArguments()[0];
				Type valueType = ptype.getActualTypeArguments()[1];

				Object key = null;
				Object value = null;
				for(Map.Entry<?, ?> entry : srcValue.entrySet()){
					if(isContainerKeyCopyValueOrRef(cloneable)){
//						key = newBeanCopier((Class<?>)keyType).fromObject(entry.getKey());
						key = fromObject(entry.getKey(), (Class<?>)keyType);
					}else{
						key = entry.getKey();
					}
					if(isContainerValueCopyValueOrRef(cloneable)){
//						value = newBeanCopier((Class<?>)valueType).fromObject(entry.getValue());
						value = fromObject(entry.getValue(), (Class<?>)valueType);
					}else{
						value = entry.getValue();
					}
					map.put(key, value);
				}
			}else{
				//不是泛型的话，直接使用源对象的类型来复制
				Object key = null;
				Object value = null;
				for(Map.Entry<?, ?> entry : srcValue.entrySet()){
					if(isContainerKeyCopyValueOrRef(cloneable)){
//						key = newBeanCopier(entry.getKey().getClass()).fromObject(entry.getKey());
						key = fromObject(entry.getKey(), entry.getKey().getClass());
					}else{
						key = entry.getKey();
					}
					if(isContainerValueCopyValueOrRef(cloneable)){
//						value = newBeanCopier(entry.getValue().getClass()).fromObject(entry.getValue());
						value = fromObject(entry.getValue(), entry.getValue().getClass());
					}else{
						value = entry.getValue();
					}
					map.put(key, value);
				}
			}
		}
		
//		ReflectionUtils.invokeMethod(toProperty.getWriteMethod(), target, map);
		targetBeanWrapper.setPropertyValue(toProperty.getName(), map);
	}

	private Object getPropertyValue(BeanWrapper srcBean, PropertyDescriptor property){
		String targetPropertyName = property.getName();
		Object srcValue = null;
		if(propertyNameConvertor!=null){
			srcValue = getPropertyValue(srcBean, targetPropertyName);
			if(srcValue==null){
    			srcValue = getPropertyValue(srcBean, propertyNameConvertor.convert(targetPropertyName));
			}
		}else{
			srcValue = getPropertyValue(srcBean, targetPropertyName);
		}
		return srcValue;
	}

	/***
	 * 如果原对象没有目标对象的属性，则返回null
	 * @param srcBean
	 * @param targetPropertyName
	 * @return
	 */
	private Object getPropertyValue(BeanWrapper srcBean, String targetPropertyName){
		Object srcValue = null;
		if(srcBean.isReadableProperty(targetPropertyName)){
			srcValue = srcBean.getPropertyValue(targetPropertyName);
		}
		return srcValue;
	}

	public void setPropertyNameConvertor(PropertyNameConvertor propertyNameConvertor) {
		this.propertyNameConvertor = propertyNameConvertor;
	}

	/*public BeanCopier<T> ignoreNull() {
		this.ignoreNull = true;
		return this;
	}*/
	
}
