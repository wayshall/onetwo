package org.onetwo.plugins.jsonrpc.server.core;

public class JsonRpcSerivceFoundEvent {
	final private String name;
	final private Object service;
	public JsonRpcSerivceFoundEvent(String name, Object service) {
		super();
		this.name = name;
		this.service = service;
	}
	public String getName() {
		return name;
	}
	public Object getService() {
		return service;
	}
	
	
}
