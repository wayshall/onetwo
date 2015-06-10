package org.onetwo.plugins.jsonrpc.server.core;

public class JsonRpcSerivceFoundEvent {
	final private Class<?> interfaceClass;
	final private Object service;
	public JsonRpcSerivceFoundEvent(Class<?> interfaceClass, Object service) {
		super();
		this.interfaceClass = interfaceClass;
		this.service = service;
	}
	public String getInterfaceName() {
		return interfaceClass.getName();
	}
	public Object getService() {
		return service;
	}
	
	
}
