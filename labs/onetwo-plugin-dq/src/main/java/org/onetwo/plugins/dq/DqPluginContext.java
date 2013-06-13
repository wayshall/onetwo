package org.onetwo.plugins.dq;

import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=DqPlugin.class)
public class DqPluginContext {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";

//	@Resource
//	private JFishSimpleCacheManagerImpl jfishSimpleCacheManager;
	
	@Bean
	public JFishEntityManagerLifeCycleListener dynamicNamedQueryDaoFactory(){
		DefaultQueryObjectFactoryManager listener = new DefaultQueryObjectFactoryManager();
		
		JDKDynamicProxyCreator creator = new JDKDynamicProxyCreator();
//		Cache methodCache = jfishSimpleCacheManager.addCacheByName(HANDLER_METHOD_CACHE);
//		creator.setMethodCache(methodCache);
		listener.setQueryObjectFactory(creator);
		return listener;
	}
	
}
