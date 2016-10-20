package org.onetwo.common.spring.utils;

import java.util.Enumeration;
import java.util.Properties;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;

public class BeanPropertiesMapper {
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private Properties config;
	final private String prefix;
	public BeanPropertiesMapper(Properties config, String prefix) {
		super();
		this.config = config;
		this.prefix = prefix;
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
					bw.setPropertyValue(propertyName, value);
					if(logger.isDebugEnabled()){
						logger.debug("set property: {}={} ", propertyName, value);
					}
				}else{
//					logger.info("ignore property: {}={}", propertyName, value);
				}
			}else{
				bw.setPropertyValue(propertyName, value);
				if(logger.isDebugEnabled()){
					logger.debug("set property: {}={} ", propertyName, value);
				}
			}
		}
	}
	
}
