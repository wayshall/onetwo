package org.onetwo.plugins.dq;

import java.util.Collection;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespaceProperties;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DefaultQueryObjectFactoryManager implements ApplicationContextAware, FileNamedQueryFactoryListener<NamespaceProperty> {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private NamespacePropertiesManager<NamespaceProperty> namespacePropertiesManager;
	
	private ApplicationContext applicationContext;
	private QueryObjectFactory queryObjectFactory;
	
	

	@Override
	public void onInitialized(BaseEntityManager baseEntityManager, FileNamedQueryFactory<NamespaceProperty> fq) {
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
		Collection<NamespaceProperties<NamespaceProperty>> namespacelist = namespacePropertiesManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		String beanName = null;
		for(NamespaceProperties<? extends NamespaceProperty> nsp : namespacelist){
			if(nsp.isGlobal())
				continue;
			dqInterface = ReflectUtils.loadClass(nsp.getNamespace());
			beanName = StringUtils.toClassName(dqInterface.getSimpleName());
			sbr.registerSingleton(beanName, this.queryObjectFactory.createQueryObject(baseEntityManager, dqInterface));
			logger.info("register dynamic query dao {} ", beanName);
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
