package org.onetwo.plugins.dq;

import java.util.Collection;

import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.QueryProvideManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfo;
import org.onetwo.plugins.dq.annotations.QueryProvider;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DefaultQueryObjectFactoryManager implements ApplicationContextAware, FileNamedQueryFactoryListener {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private NamespacePropertiesManager<? extends NamespaceProperty> namespacePropertiesManager;
	
	private ApplicationContext applicationContext;
	private QueryObjectFactory queryObjectFactory;
	
	

	@Override
	public void onInitialized(QueryProvideManager baseEntityManager, FileNamedQueryFactory<? extends NamespaceProperty> fq) {
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
		QueryProvideManager cq = null;
		for(PropertiesNamespaceInfo<NamespaceProperty> nsp : (Collection<PropertiesNamespaceInfo<NamespaceProperty>>)namespacelist){
			if(nsp.isGlobal())
				continue;
			
			dqInterface = loadQueryClass(nsp);
			QueryProvider creator = dqInterface.getAnnotation(QueryProvider.class);
			if(creator!=null){
				if(StringUtils.isNotBlank(creator.value())){
					cq = SpringUtils.getBean(applicationContext, creator.value());
				}else{
					cq = SpringUtils.getHighestOrder(applicationContext, creator.beanClass());
				}
				if(cq==null){
					throw new BaseException("can not found QueryProvideManager for QueryProvider: " + creator.value()+", "+creator.beanClass());
				}
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
