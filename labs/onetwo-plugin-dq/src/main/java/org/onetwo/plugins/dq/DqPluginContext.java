package org.onetwo.plugins.dq;

import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=DqPlugin.class)
public class DqPluginContext {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";
	
	@Bean
	public JFishEntityManagerLifeCycleListener dynamicNamedQueryDaoFactory(){
		DefaultDynamicNamedQueryDaoFactory listener = new DefaultDynamicNamedQueryDaoFactory();
		listener.setMethodCache(new ConcurrentMapCache(HANDLER_METHOD_CACHE));
		return listener;
	}
	
}
