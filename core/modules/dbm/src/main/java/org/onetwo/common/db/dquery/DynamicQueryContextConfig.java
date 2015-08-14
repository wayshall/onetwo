package org.onetwo.common.db.dquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DynamicQueryContextConfig  {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";

//	private ApplicationContext applicationContext;
	/*@Autowired
	private QueryProvideManager baseEntityManager;
	@Autowired
	private FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory;*/
	
	

	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	@Bean
	@Autowired
	public DynamicQueryObjectRegister queryObjectFactoryManager(){
		DynamicQueryObjectRegister queryFactory = new DynamicQueryObjectRegister();
		/*queryFactory.setQueryObjectFactory(queryObjectFactory());
		queryFactory.setBaseEntityManager(baseEntityManager);
		queryFactory.setFileNamedQueryFactory(fileNamedQueryFactory);*/
		return queryFactory;
	}
	
	/*@Bean
	public QueryObjectFactory queryObjectFactory(){
		JDKDynamicProxyCreator creator = new JDKDynamicProxyCreator();
//		creator.setApplicationContext(applicationContext);
		return creator;
	}*/
	
	
}
