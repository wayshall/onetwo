package org.onetwo.plugins.jsonrpc.server.core;

import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import com.google.common.eventbus.EventBus;

public class JsonRpcSerivceRepository {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ConcurrentHashMap<String, Object> serviceCache = new ConcurrentHashMap<>();
	private EventBus jsonRpcServiceEventBus = new EventBus("jsonRpcServiceEventBus");
	
	
	public JsonRpcSerivceRepository() {
		super();
	}


	public <T> T findService(String className){
		return (T)serviceCache.get(className);
	}
	
	final public JsonRpcSerivceRepository registerService(String name, Object service){
		this.serviceCache.put(name, service);
		logger.info("found jsonrpcservice: " + service.getClass().getName());
		this.jsonRpcServiceEventBus.post(new JsonRpcSerivceFoundEvent(name, service));
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
