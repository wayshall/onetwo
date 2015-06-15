package org.onetwo.plugins.jsonrpc.client.core;

import com.google.common.eventbus.Subscribe;

public interface JsonRpcClientListener {
	
	@Subscribe
	void onCreated(JsonRpcClientCreatedEvent event);

}
