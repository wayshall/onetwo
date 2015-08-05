package org.onetwo.common.db.dquery;

import javax.annotation.Resource;

import org.onetwo.common.db.filequery.FileNamedQueryFactory;
import org.onetwo.common.db.filequery.NamespaceProperty;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.spring.sql.SqlParamterPostfixFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DynamicQueryContextConfig {

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";

	@Resource
	private ApplicationContext applicationContext;
	@Autowired
	private QueryProvideManager baseEntityManager;
	@Autowired
	private FileNamedQueryFactory<? extends NamespaceProperty> fileNamedQueryFactory;
	
	
	@Bean
	public DynamicQueryObjectRegister queryObjectFactoryManager(){
		DynamicQueryObjectRegister queryFactory = new DynamicQueryObjectRegister();
		queryFactory.setQueryObjectFactory(queryObjectFactory());
		queryFactory.setBaseEntityManager(baseEntityManager);
		queryFactory.setFileNamedQueryFactory(fileNamedQueryFactory);
		return queryFactory;
	}
	
	@Bean
	public QueryObjectFactory queryObjectFactory(){
		JDKDynamicProxyCreator creator = new JDKDynamicProxyCreator();
		creator.setApplicationContext(applicationContext);
		return creator;
	}
	
	@Bean
	public SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry(){
		return new SqlParamterPostfixFunctions();
	}
	
}
