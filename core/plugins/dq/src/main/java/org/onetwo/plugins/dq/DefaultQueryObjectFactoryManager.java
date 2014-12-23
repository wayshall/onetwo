package org.onetwo.plugins.dq;

import java.util.Collection;

import org.onetwo.common.db.QueryProvider;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfo;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.onetwo.plugins.dq.annotations.QueryCreator;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DefaultQueryObjectFactoryManager implements ApplicationContextAware, FileNamedQueryFactoryListener {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private NamespacePropertiesManager<? extends NamespaceProperty> namespacePropertiesManager;
	
	private ApplicationContext applicationContext;
	private QueryObjectFactory queryObjectFactory;
	
	

	@Override
	public void onInitialized(QueryProvider baseEntityManager, FileNamedQueryFactory<? extends NamespaceProperty> fq) {
		Assert.notNull(queryObjectFactory);
		this.namespacePropertiesManager = fq.getNamespacePropertiesManager();
		
		
		BeanFactory bf = null;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !SingletonBeanRegistry.class.isInstance(bf)){
			logger.warn("not SingletonBeanRegistry, ignore...");
			return ;
		}
		
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		Collection<?> namespacelist = namespacePropertiesManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		String beanName = null;
		QueryProvider cq = null;
		for(PropertiesNamespaceInfo<NamespaceProperty> nsp : (Collection<PropertiesNamespaceInfo<NamespaceProperty>>)namespacelist){
			if(nsp.isGlobal())
				continue;
			
			dqInterface = loadQueryClass(nsp);
			QueryCreator creator = dqInterface.getAnnotation(QueryCreator.class);
			if(creator!=null){
				Object cqbean = SpringUtils.getBean(applicationContext, creator.value());
				if(!QueryProvider.class.isInstance(cqbean)){
					throw new BaseException("QueryCreator must be a instance of CreateQueryable : " + creator.value());
				}
				cq = (QueryProvider) cqbean;
			}else{
				cq = baseEntityManager;
			}
			
			beanName = StringUtils.toClassName(dqInterface.getSimpleName());
			sbr.registerSingleton(beanName, this.queryObjectFactory.createQueryObject(cq, dqInterface));
			logger.info("register dynamic query dao {} ", beanName);
		}
	}

	private Class<?> loadQueryClass(PropertiesNamespaceInfo<NamespaceProperty> nsp){
		try {
			return ReflectUtils.loadClass(nsp.getNamespace());
		} catch (Exception e) {
			throw new BaseException("load class for query error: " + e.getMessage(), e);
		}
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	public void setQueryObjectFactory(QueryObjectFactory queryObjectFactory) {
		this.queryObjectFactory = queryObjectFactory;
	}



}
