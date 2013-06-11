package org.onetwo.plugins.dq;

import java.util.Collection;

import javax.annotation.Resource;

import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.spring.JFishEntityManagerLifeCycleListener;
import org.onetwo.common.fish.spring.JFishNamedFileQueryInfo;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.cache.JFishSimpleCacheManagerImpl;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.NamespaceProperties;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

public class DefaultDynamicNamedQueryDaoFactory implements JFishEntityManagerLifeCycleListener, ApplicationContextAware {
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	public final static String HANDLER_METHOD_CACHE = "DQ_HANDLER_METHOD_CACHE";
//	private String packageToScan;
	
	private JFishNamedFileQueryManager jfishNamedFileQueryManager;
//	private ClassPool classPool = ClassPool.getDefault();
	private ApplicationContext applicationContext;
	
	@Resource
	private JFishSimpleCacheManagerImpl jfishSimpleCacheManager;
	
	@Override
	public void onInit(JFishEntityManager em) {
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
		
		Cache cache = this.jfishSimpleCacheManager.addCacheByName(HANDLER_METHOD_CACHE);
		
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		NamespacePropertiesManager<JFishNamedFileQueryInfo> namespaceManager = (NamespacePropertiesManager<JFishNamedFileQueryInfo>)this.jfishNamedFileQueryManager;
		Collection<NamespaceProperties<JFishNamedFileQueryInfo>> namespacelist = namespaceManager.getAllNamespaceProperties();
		
		Class<?> dqInterface = null;
		DynamicQueryProxyFactory factory = null;
		String beanName = null;
		for(NamespaceProperties<JFishNamedFileQueryInfo> nsp : namespacelist){
			if(nsp.isGlobal())
				continue;
			dqInterface = ReflectUtils.loadClass(nsp.getNamespace());
			factory = new DynamicQueryHandler(em, cache, dqInterface);
			beanName = StringUtils.toClassName(dqInterface.getSimpleName());
			sbr.registerSingleton(beanName, factory.getProxyObject());
			logger.info("register dynamic query {} ", beanName);
		}
	}

	@Override
	public void onDestroy(JFishEntityManager dao) {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
}
