package org.onetwo.plugins.jsonrpc.client.core;

import org.springframework.beans.factory.BeanFactory;

public class JsonRpcClientCreatedEvent {
	private JsonRpcClientRepository source;
	private BeanFactory beanFactory;
	private RpcClientFacotry rpcCactory;
	private Class<?> interfaceClass;
	
	public JsonRpcClientCreatedEvent(JsonRpcClientRepository source,
			BeanFactory beanFactory, RpcClientFacotry rpcCactory,
			Class<?> interfaceClass) {
		super();
		this.source = source;
		this.beanFactory = beanFactory;
		this.rpcCactory = rpcCactory;
		this.interfaceClass = interfaceClass;
	}

	public RpcClientFacotry getRpcCactory() {
		return rpcCactory;
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void registerClientBean(String beanName, Object clientObj){
		source.registerClientBean(beanFactory, interfaceClass, beanName, clientObj);
	}

	public JsonRpcClientRepository getSource() {
		return source;
	}
	
	
}
