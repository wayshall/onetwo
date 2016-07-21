package org.onetwo.common.db.dquery;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


public class DynamicQueryObjectRegisterPostProcessor implements /*FileNamedQueryFactoryListener, */ BeanDefinitionRegistryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());


	private String databaseName;
	
	public void setDataBaseName(String databaseName) {
		this.databaseName = databaseName;
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(registry);
		register.setDatabaseName(databaseName);
		register.registerQueryBeans();
	}
	
}
