package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientCreatedEvent;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientListener;

public class DirectServerEndpointClientRegister implements JsonRpcClientListener{

	@Override
	public void onCreated(JsonRpcClientCreatedEvent event) {
		Class<?> interfaceClass = event.getInterfaceClass();
		Object clientObj = event.getRpcCactory().create(interfaceClass);
		String beanName = StringUtils.uncapitalize(interfaceClass.getSimpleName());
		event.registerClientBean(beanName, clientObj);
	}
	
}
