package org.onetwo.plugins.zkclient;

import java.util.Map;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ZkEventListenerRegister implements BeanFactoryPostProcessor {
	private final static Logger logger = JFishLoggerFactory.getLogger(ZkEventListenerRegister.class);
	
//	@Resource
	private Zkclienter zkclienter;
	
	
	public ZkEventListenerRegister(Zkclienter zkclienter) {
		super();
		this.zkclienter = zkclienter;
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Map<String, ZkEventListener> listeners = beanFactory.getBeansOfType(ZkEventListener.class);
		listeners.forEach((k, v)->{
			zkclienter.register(v);
			logger.info("reigistered ZkWatchedEventListener : {}", k);
		});
	}

}
