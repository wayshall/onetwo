package org.onetwo.plugins.dq;

import org.onetwo.common.fish.spring.JFishDaoLifeCycleListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=DqPlugin.class)
public class DqPluginContext {
	
	@Bean
	public JFishDaoLifeCycleListener dynamicNamedQueryDaoFactory(){
		return new DefaultDynamicNamedQueryDaoFactory();
	}
	
}
