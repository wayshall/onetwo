package org.onetwo.plugins.zkclient.core;

import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

public class ZkEventListenerRegister implements InitializingBean {
	private final static Logger logger = JFishLoggerFactory.getLogger(ZkEventListenerRegister.class);
	
//	@Resource
	private Zkclienter zkclienter;
	@Resource
	private ApplicationContext applicationContext;
	
	
	public ZkEventListenerRegister(Zkclienter zkclienter) {
		super();
		this.zkclienter = zkclienter;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, ZkEventListener> listeners = SpringUtils.getBeansAsMap(applicationContext, ZkEventListener.class);
		listeners.forEach((k, v)->{
			zkclienter.register(v);
			logger.info("reigistered ZkWatchedEventListener : {}", k);
		});
	}

/*
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Map<String, ZkEventListener> listeners = beanFactory.getBeansOfType(ZkEventListener.class);
		listeners.forEach((k, v)->{
			zkclienter.register(v);
			logger.info("reigistered ZkWatchedEventListener : {}", k);
		});
	}*/

}
