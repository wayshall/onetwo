package org.onetwo.plugins.dq;

import javax.annotation.Resource;

import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DqPluginContext {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";

	@Resource
	private ApplicationContext applicationContext;
//	@Resource
//	private JFishSimpleCacheManagerImpl jfishSimpleCacheManager;
	
	
	@Bean
	public FileNamedQueryFactoryListener queryObjectFactoryManager(){
		DefaultQueryObjectFactoryManager queryFactory = new DefaultQueryObjectFactoryManager();
//		Cache methodCache = jfishSimpleCacheManager.addCacheByName(HANDLER_METHOD_CACHE);
//		creator.setMethodCache(methodCache);
		queryFactory.setQueryObjectFactory(queryObjectFactory());
		return queryFactory;
	}
	
	@Bean
	public QueryObjectFactory queryObjectFactory(){
		JDKDynamicProxyCreator creator = new JDKDynamicProxyCreator();
		creator.setApplicationContext(applicationContext);
		return creator;
	}
	
}
