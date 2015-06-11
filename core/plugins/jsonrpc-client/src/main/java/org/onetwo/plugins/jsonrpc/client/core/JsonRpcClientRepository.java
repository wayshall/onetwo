package org.onetwo.plugins.jsonrpc.client.core;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.EventBus;

public class JsonRpcClientRepository implements InitializingBean {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ConcurrentHashMap<String, Object> clientCache = new ConcurrentHashMap<>();
	private EventBus jsonRpcClientEventBus = new EventBus("jsonRpcClientEventBus");
//	private RpcClientPluginConfig rpcClientPluginConfig;

	@Resource
	private ApplicationContext applicationContext;
	
	public JsonRpcClientRepository() {
		super();
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		/*if(applicationContext!=null){
			SpringUtils.getBeans(applicationContext, JsonRpcClientListener.class)
						.forEach(listener->{
							registerListener(listener);
							logger.info("registered JsonRpcClientListener : {}", listener);
						});
		}*/
	}


	public <T> T findService(String className){
		return (T)clientCache.get(className);
	}
	
	final public JsonRpcClientRepository registerClient(BeanFactory beanFactory, RpcClientFacotry rpcCactory, Class<?> interfaceClass){
		this.jsonRpcClientEventBus.post(new JsonRpcClientCreatedEvent(this, beanFactory, rpcCactory, interfaceClass));
		return this;
	}
	
	void registerClientBean(BeanFactory beanFactory, Class<?> interfaceClass, String beanName, Object clientObj){
		if(clientCache.containsKey(beanName)){
			throw new BaseException("duplicate jsonrpc client found: " + beanName + ", interface: " + interfaceClass);
		}
		SpringUtils.registerSingleton(beanFactory, beanName, clientObj);
		this.clientCache.put(beanName, clientObj);
		logger.info("registerClient beanName:{}, interface: {}", beanName, interfaceClass);
	}
	
	final public JsonRpcClientRepository registerListener(JsonRpcClientListener listener){
		jsonRpcClientEventBus.register(listener);
		logger.info("find jsonrpcservice listener: " + listener);
		return this;
	}
	
	/*final public JsonRpcSerivceRepository registerServices(Map<String, Object> services){
		this.serviceCache.putAll(services);
		return this;
	}*/

}
