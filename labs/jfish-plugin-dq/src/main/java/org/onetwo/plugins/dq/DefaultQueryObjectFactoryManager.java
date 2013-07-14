package org.onetwo.plugins.dq;

import java.util.Collection;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.onetwo.common.fish.spring.JFishNamedFileQueryInfo;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.NamespaceProperties;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DefaultQueryObjectFactoryManager implements JFishEntityManagerLifeCycleListener, ApplicationContextAware {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private JFishNamedFileQueryManager jfishNamedFileQueryManager;
	private ApplicationContext applicationContext;
	private QueryObjectFactory queryObjectFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onInit(JFishEntityManager em) {
		Assert.notNull(queryObjectFactory);
		
		this.jfishNamedFileQueryManager = em.getJfishDao().getJfishFileQueryFactory();
		if(!(jfishNamedFileQueryManager instanceof NamespacePropertiesManager)){
			logger.warn("jfishNamedFileQueryManager is not a instance of NamespacePropertiesManager, ignore generate dynamic query class.");
			return ;
		}
		
		BeanFactory bf = null;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !SingletonBeanRegistry.class.isInstance(bf)){
			logger.warn("not SingletonBeanRegistry, ignore...");
			return ;
		}
		
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		NamespacePropertiesManager<JFishNamedFileQueryInfo> namespaceManager = (NamespacePropertiesManager<JFishNamedFileQueryInfo>)this.jfishNamedFileQueryManager;
		Collection<NamespaceProperties<JFishNamedFileQueryInfo>> namespacelist = namespaceManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		String beanName = null;
		for(NamespaceProperties<JFishNamedFileQueryInfo> nsp : namespacelist){
			if(nsp.isGlobal())
				continue;
			dqInterface = ReflectUtils.loadClass(nsp.getNamespace());
			beanName = StringUtils.toClassName(dqInterface.getSimpleName());
			sbr.registerSingleton(beanName, this.queryObjectFactory.createQueryObject(em, dqInterface));
			logger.info("register dynamic query dao {} ", beanName);
		}
	}
	
	
	@Override
	public void onDestroy(JFishEntityManager dao) {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	public QueryObjectFactory getQueryObjectFactory() {
		return queryObjectFactory;
	}


	public void setQueryObjectFactory(QueryObjectFactory queryObjectFactory) {
		this.queryObjectFactory = queryObjectFactory;
	}

}
