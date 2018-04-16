package org.onetwo.cloud.feign.local;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
//@AutoConfigureBefore(FeignAutoConfiguration.classS)
public class LocalFeignConfiguration /*implements ImportBeanDefinitionRegistrar, BeanFactoryAware*/ {
	/*private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (beanFactory instanceof ConfigurableListableBeanFactory) {
			this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
		}
	}
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registry.registerBeanDefinition("LocalFeignBeanPostProcessor", new RootBeanDefinition(LocalFeignBeanPostProcessor.class));
	}*/

	/*@Bean
	static public LocalFeignBeanPostProcessor localFeignBeanPostProcessor(){
		return new LocalFeignBeanPostProcessor();
	}*/

	/*@Bean
	static public LocalFeignBeanDefinitionRegistryPostProcessor localFeignBeanDefinitionRegistryPostProcessor(){
		return new LocalFeignBeanDefinitionRegistryPostProcessor();
	}*/

	
}
