package org.onetwo.common.db.dquery;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


public class DynamicQueryObjectRegisterPostProcessor implements /*FileNamedQueryFactoryListener, */ BeanDefinitionRegistryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());


	private DataBase database;
	
	public void setDatabase(DataBase database) {
		this.database = database;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		DynamicQueryObjectRegister register = new DynamicQueryObjectRegister(registry);
		register.setDatabase(database);
		register.registerQueryBeans();
	}
	
}
