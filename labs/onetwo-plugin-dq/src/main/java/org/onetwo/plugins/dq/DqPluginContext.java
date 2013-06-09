package org.onetwo.plugins.dq;

import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=DqPlugin.class)
public class DqPluginContext {
	
	@Bean
	public JFishEntityManagerLifeCycleListener dynamicNamedQueryDaoFactory(){
		return new DefaultDynamicNamedQueryDaoFactory();
	}
	
}
