package org.onetwo.common.spring.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.utils.ConfigableBeanMapper.BeanAccessors;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.ConfigurablePropertyAccessor;

public class BeanPropertiesMapper {
	
	
	public static BeanPropertiesMapper props(Properties config, String prefix, boolean ignoreFoundProperty){
		return new BeanPropertiesMapper(config, prefix, ignoreFoundProperty);
	}
	
	public static BeanPropertiesMapper ignoreNotFoundProperty(Properties config){
		return new BeanPropertiesMapper(config, null, true);
	}
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private Properties config;
	final private String prefix;
	private boolean ignoreNotFoundProperty = false;
	private BeanAccessors beanAccessors = BeanAccessors.PROPERTY;
	private boolean ignoreBlankString;
	
	public BeanPropertiesMapper(Properties config, String prefix) {
		this(config, prefix, false);
	}
	public BeanPropertiesMapper(Properties config, String prefix, boolean ignoreFoundProperty) {
		super();
		this.config = config;
		this.prefix = prefix;
		this.ignoreNotFoundProperty = ignoreFoundProperty;
	}
	
	public BeanPropertiesMapper ignoreBlankString() {
		this.ignoreBlankString = true;
		return this;
	}
	
	public void setIgnoreBlankString(boolean ignoreBlankString) {
		this.ignoreBlankString = ignoreBlankString;
	}

	public void setBeanAccessors(BeanAccessors beanAccessors) {
		this.beanAccessors = beanAccessors;
	}

	public BeanPropertiesMapper fieldAccessors() {
		this.beanAccessors = BeanAccessors.FIELD;
		return this;
	}

	public void mapToObject(Object obj) {
		if(config==null || config.isEmpty()){
			return ;
		}
		boolean hasPrefix = StringUtils.isNotBlank(prefix);
		
		ConfigurablePropertyAccessor bw = beanAccessors.createAccessor(obj);
		Enumeration<?> names = config.propertyNames();
		while(names.hasMoreElements()){
			String propertyName = names.nextElement().toString();
			String value = config.getProperty(propertyName);
			if(value==null){
				continue;
			}
			if(StringUtils.isBlank(value) && ignoreBlankString){
				continue;
			}
			if(hasPrefix){
				if(propertyName.startsWith(prefix)){
					propertyName = propertyName.substring(prefix.length());
					setPropertyValue(obj, bw, propertyName, value);
				}
			}else{
				setPropertyValue(obj, bw, propertyName, value);
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
