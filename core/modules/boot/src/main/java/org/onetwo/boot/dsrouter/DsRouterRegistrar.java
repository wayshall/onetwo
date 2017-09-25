package org.onetwo.boot.dsrouter;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

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
