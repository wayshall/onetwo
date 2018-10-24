package org.onetwo.common.spring.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

public class ConfigableBeanMapper {
	
	public static enum BeanAccessors {
		PROPERTY {
			@Override
			public ConfigurablePropertyAccessor createAccessor(Object obj) {
				ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(obj);
				accessor.setAutoGrowNestedPaths(true);
				return accessor;
			}
		},
		FIELD{
			@Override
			public ConfigurablePropertyAccessor createAccessor(Object obj) {
				ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(obj);
				accessor.setAutoGrowNestedPaths(true);
				return accessor;
			}
		};
		
		abstract public ConfigurablePropertyAccessor createAccessor(Object obj);
	}
	
	public static ConfigableBeanMapper props(Map<String,Object> config, String prefix, boolean ignoreFoundProperty){
		return new ConfigableBeanMapper(config, prefix, ignoreFoundProperty);
	}
	
	public static ConfigableBeanMapper ignoreNotFoundProperty(Map<String,Object> config){
		return new ConfigableBeanMapper(config, null, true);
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private Map<String,Object> config;
	final private String prefix;
	private boolean ignoreNotFoundProperty = false;
	private BeanAccessors beanAccessors = BeanAccessors.PROPERTY;
	private boolean ignoreBlankString;
	
	public ConfigableBeanMapper(Map<String,Object> config, String prefix) {
		this(config, prefix, false);
	}
	public ConfigableBeanMapper(Map<String,Object> config, String prefix, boolean ignoreFoundProperty) {
		super();
		this.config = config;
		this.prefix = prefix;
		this.ignoreNotFoundProperty = ignoreFoundProperty;
	}
	
	public ConfigableBeanMapper ignoreBlankString() {
		this.ignoreBlankString = true;
		return this;
	}
	
	public void setIgnoreBlankString(boolean ignoreBlankString) {
		this.ignoreBlankString = ignoreBlankString;
	}

	public void setBeanAccessors(BeanAccessors beanAccessors) {
		this.beanAccessors = beanAccessors;
	}

	public ConfigableBeanMapper fieldAccessors() {
		this.beanAccessors = BeanAccessors.FIELD;
		return this;
	}

	public void mapToObject(Object obj) {
		if(config==null || config.isEmpty()){
			return ;
		}
		boolean hasPrefix = StringUtils.isNotBlank(prefix);
		
		ConfigurablePropertyAccessor bw = beanAccessors.createAccessor(obj);
//		Enumeration<?> names = config.propertyNames();
//		while(names.hasMoreElements()){
		for(Entry<String, Object> entry : config.entrySet()){
//			String propertyName = names.nextElement().toString();
//			String value = config.getProperty(propertyName);
			if(entry.getValue()==null){
				continue;
			}
			String propertyName = entry.getKey();
			String text = entry.getValue().toString();
			if(StringUtils.isBlank(text) && ignoreBlankString){
				continue;
			}
			if(hasPrefix){
				if(propertyName.startsWith(prefix)){
					propertyName = propertyName.substring(prefix.length());
					setPropertyValue(obj, bw, propertyName, text);
				}
			}else{
				setPropertyValue(obj, bw, propertyName, text);
			}
		}
	}
	
	
	protected void setPropertyValue(Object obj, ConfigurablePropertyAccessor bw, String propertyName, Object value){
		if(!bw.isWritableProperty(propertyName)){
			if(!ignoreNotFoundProperty){
				throw new NoSuchElementException("no setter found for property: " + propertyName+", target: " + obj);
			}
			logger.debug("ignore property: {}={} ", propertyName, value);
			return ;
		}
		bw.setPropertyValue(propertyName, value);
		if(logger.isDebugEnabled()){
			logger.debug("set property: {}={} ", propertyName, value);
		}
	}
	
}
