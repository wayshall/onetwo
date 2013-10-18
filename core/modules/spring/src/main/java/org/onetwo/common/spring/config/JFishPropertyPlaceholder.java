package org.onetwo.common.spring.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class JFishPropertyPlaceholder extends PropertyPlaceholderConfigurer {
	private Properties mergedConfig;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		this.mergedConfig  = new Properties();
		this.mergedConfig.putAll(props);
	}

	public Properties getMergedConfig() {
		return mergedConfig;
	}
	
}
