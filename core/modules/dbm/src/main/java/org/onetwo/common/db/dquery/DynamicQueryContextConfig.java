package org.onetwo.common.db.dquery;

import javax.annotation.Resource;

import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DynamicQueryContextConfig {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";

	@Resource
	private ApplicationContext applicationContext;
	
	
	@Bean
	public FileNamedQueryFactoryListener queryObjectFactoryManager(){
		DefaultQueryObjectFactoryManager queryFactory = new DefaultQueryObjectFactoryManager();
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
