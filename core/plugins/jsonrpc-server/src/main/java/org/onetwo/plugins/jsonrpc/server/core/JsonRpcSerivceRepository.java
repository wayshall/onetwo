package org.onetwo.plugins.jsonrpc.server.core;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import com.google.common.eventbus.EventBus;

public class JsonRpcSerivceRepository implements InitializingBean {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ConcurrentHashMap<String, Object> serviceCache = new ConcurrentHashMap<>();
	private EventBus jsonRpcServiceEventBus = new EventBus("jsonRpcServiceEventBus");
	
	@Resource
	private ApplicationContext applicationContext;
	
	public JsonRpcSerivceRepository() {
		super();
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if(applicationContext!=null){
			SpringUtils.getBeans(applicationContext, JsonRpcSerivceListener.class)
						.forEach(listener->{
							registerListener(listener);
							logger.info("registered JsonRpcSerivceListener : {}", listener);
						});
		}
	}


	public <T> T findService(String className){
		return (T)serviceCache.get(className);
	}
	
	final public JsonRpcSerivceRepository registerService(Class<?> interfaceClass, Object service){
		this.serviceCache.put(interfaceClass.getName(), service);
		logger.info("found jsonrpcservice: " + service.getClass().getName());
		this.jsonRpcServiceEventBus.post(new JsonRpcSerivceFoundEvent(interfaceClass, service));
		return this;
	}
	
	final public JsonRpcSerivceRepository registerListener(JsonRpcSerivceListener listener){
		jsonRpcServiceEventBus.register(listener);
		logger.info("find jsonrpcservice listener: " + listener);
		return this;
	}
	
	/*final public JsonRpcSerivceRepository registerServices(Map<String, Object> services){
		this.serviceCache.putAll(services);
		return this;
	}*/

}
