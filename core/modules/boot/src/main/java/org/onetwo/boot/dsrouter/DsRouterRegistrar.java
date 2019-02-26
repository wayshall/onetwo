package org.onetwo.boot.dsrouter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author wayshall
 * <br/>
 */
public class DsRouterRegistrar implements BeanFactoryPostProcessor /*ImportBeanDefinitionRegistrar, EnvironmentAware*/ {
	
	@Autowired
	private DatasourceRouterProperties datasourceRouterProperties;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DatasourceRouterProperties ds = beanFactory.getBean(DatasourceRouterProperties.class);
		System.out.println("test:"+datasourceRouterProperties);
	}

	
	/*@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "jfish.dsRouter");
		Map<String, Object> datasource = resolver.getSubProperties("datasources");
		System.out.println("test:"+datasource);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}*/
	
	
	

}
