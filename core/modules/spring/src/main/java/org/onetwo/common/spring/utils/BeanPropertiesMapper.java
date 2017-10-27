package org.onetwo.common.spring.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;

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
	
	public BeanPropertiesMapper(Properties config, String prefix) {
		this(config, prefix, false);
	}
	public BeanPropertiesMapper(Properties config, String prefix, boolean ignoreFoundProperty) {
		super();
		this.config = config;
		this.prefix = prefix;
		this.ignoreNotFoundProperty = ignoreFoundProperty;
	}
	
	public void mapToObject(Object obj) {
		Assert.notEmpty(config);
		boolean hasPrefix = StringUtils.isNotBlank(prefix);
		
		BeanWrapper bw = SpringUtils.newBeanWrapper(obj);
		Enumeration<?> names = config.propertyNames();
		while(names.hasMoreElements()){
			String propertyName = names.nextElement().toString();
			String value = config.getProperty(propertyName);
			if(hasPrefix){
				if(propertyName.startsWith(prefix)){
					propertyName = propertyName.substring(prefix.length());
					setPropertyValue(bw, propertyName, value);
				}
			}else{
				setPropertyValue(bw, propertyName, value);
			}
		}
	}
	
	
	protected void setPropertyValue(BeanWrapper bw, String propertyName, Object value){
		if(!bw.isWritableProperty(propertyName)){
			if(!ignoreNotFoundProperty){
				throw new NoSuchElementException("no setter found for property: " + propertyName);
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
