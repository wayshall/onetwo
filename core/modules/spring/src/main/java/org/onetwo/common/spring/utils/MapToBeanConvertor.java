package org.onetwo.common.spring.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

import lombok.Builder;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FieldName;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author wayshall
 * <br/>
 */
@Builder
public class MapToBeanConvertor {

	final private static Logger logger = JFishLoggerFactory.getLogger(MapToBeanConvertor.class);
	
	final protected static LoadingCache<PropertyContext, String> PROPERTIES_CACHES = CacheBuilder.newBuilder()
																.weakKeys()
																.build(new CacheLoader<PropertyContext, String>() {
																	@Override
																	public String load(PropertyContext pc) throws Exception {
																		return pc.getName();
																	}
																});
	
	private static Function<PropertyContext, String> DEFAULT_KEY_CONVERTOR = pd->{
		try {
			return PROPERTIES_CACHES.get(pd);
		} catch (Exception e) {
			logger.error("get map key error for property: {}", pd.getName());
			return pd.getName();
		}
	};

	private Function<PropertyContext, String> keyConvertor = DEFAULT_KEY_CONVERTOR;
	
	public <T> T toBean(Map<String, Object> propValues, Class<T> beanClass){
		Assert.notNull(beanClass);
		Assert.notNull(propValues);
		T bean = ReflectUtils.newInstance(beanClass);
		BeanWrapper bw = SpringUtils.newBeanWrapper(bean);
		for(PropertyDescriptor pd : bw.getPropertyDescriptors()){
			PropertyContext pc = new PropertyContext(beanClass, pd);
			String key = getMapKey(pc);
			Object value = propValues.get(key);
			if(value==null){
				continue;
			}
			value = SpringUtils.getFormattingConversionService().convert(value, pd.getPropertyType());
			bw.setPropertyValue(pd.getName(), value);
		}
		return bean;
	}
	
	public void setKeyConvertor(Function<PropertyContext, String> keyConvertor) {
		this.keyConvertor = keyConvertor;
	}

	protected String getMapKey(PropertyContext pc){
		Function<PropertyContext, String> keyConvertor = this.keyConvertor;
		if(keyConvertor==null){
			keyConvertor = DEFAULT_KEY_CONVERTOR;
			this.keyConvertor = keyConvertor;
		}
		return keyConvertor.apply(pc);
	}
	
	protected static class PropertyContext {
		final private Class<?> beanClass;
		final private PropertyDescriptor propertyDescriptor;
		public PropertyContext(Class<?> beanClass, PropertyDescriptor propertyDescriptor) {
			super();
			this.beanClass = beanClass;
			this.propertyDescriptor = propertyDescriptor;
		}
		
		public String getName(){
			Method method = propertyDescriptor.getReadMethod();
			FieldName fn = method.getAnnotation(FieldName.class);
			if(fn==null){
				Field field = ReflectUtils.getIntro(getBeanClass()).getField(propertyDescriptor.getName());
				if(field!=null){
					fn = field.getAnnotation(FieldName.class);
				}
			}
			return fn==null?propertyDescriptor.getName():fn.value();
		}
		
		public Class<?> getBeanClass() {
			return beanClass;
		}

		public PropertyDescriptor getPropertyDescriptor() {
			return propertyDescriptor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((beanClass == null) ? 0 : beanClass.hashCode());
			result = prime
					* result
					+ ((propertyDescriptor == null) ? 0 : propertyDescriptor
							.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PropertyContext other = (PropertyContext) obj;
			if (beanClass == null) {
				if (other.beanClass != null)
					return false;
			} else if (!beanClass.equals(other.beanClass))
				return false;
			if (propertyDescriptor == null) {
				if (other.propertyDescriptor != null)
					return false;
			} else if (!propertyDescriptor.equals(other.propertyDescriptor))
				return false;
			return true;
		}
		
		
	}
}
