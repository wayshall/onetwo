package org.onetwo.plugins.jorm;

import org.onetwo.common.fish.spring.config.JFishOrm;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=JormPlugin.class)
@JFishOrm
public class JormPluginContext implements ApplicationContextAware {
	
	@Value("${jfish.base.packages}")
	private String jfishBasePackage;
	
	private ApplicationContext applicationContext;
	
	@Bean
	public BaseAppConfigurator appConfigurator(){
		BaseAppConfigurator config = SpringUtils.getBean(applicationContext, BaseAppConfigurator.class);
		if(config==null){
			config = new BaseAppConfigurator() {
				
				@Override
				public String getJFishBasePackage() {
					return jfishBasePackage;
				}
			};
		}
		return config;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
