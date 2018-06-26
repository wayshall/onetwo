package org.onetwo.cloud.feign.local;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wayshall
 * <br/>
 */
public class LocalFeignContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
//		applicationContext.addBeanFactoryPostProcessor(new LocalFeignBeanDefinitionRegistryPostProcessor());
	}

}
