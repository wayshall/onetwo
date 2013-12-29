package org.onetwo.common.spring.utils;

import java.util.Enumeration;
import java.util.Properties;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigableBeanByPropertiesFactoryBean<T> implements FactoryBean<T>, InitializingBean {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private T object;
	private Class<? extends T> implementClass;
	private Properties config;
	private String prefix;
	
	@Autowired
	private JFishPropertyPlaceholder configHolder;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(config==null){
			config = configHolder.getMergedConfig();
		}
		Assert.notEmpty(config);
		boolean hasPrefix = StringUtils.isNotBlank(prefix);
		
		object = BeanUtils.instantiate(implementClass);
		BeanWrapper bw = SpringUtils.newBeanWrapper(object);
		Enumeration<?> names = config.propertyNames();
		while(names.hasMoreElements()){
			String propertyName = names.nextElement().toString();
			String value = config.getProperty(propertyName);
			if(hasPrefix){
				if(propertyName.startsWith(prefix)){
					propertyName = propertyName.substring(prefix.length());
					bw.setPropertyValue(propertyName, value);
					logger.info("set property: {}={} ", propertyName, value);
				}else{
					logger.info("ignore property: {}", propertyName);
				}
			}else{
				bw.setPropertyValue(propertyName, value);
				logger.info("set property: {}={} ", propertyName, value);
			}
		}
	}

	@Override
	public T getObject() throws Exception {
		return object;
	}

	@Override
	public Class<?> getObjectType() {
		return implementClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setImplementClass(Class<? extends T> implementClass) {
		this.implementClass = implementClass;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
