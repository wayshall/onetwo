package org.onetwo.plugins.jsonrpc.server.core;

import com.google.common.eventbus.Subscribe;

public interface JsonRpcSerivceListener {
	
	@Subscribe
	void onFinded(JsonRpcSerivceFoundEvent event);

}
