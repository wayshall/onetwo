package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jsonrpc.client.core.ServerEndpointProvider;
import org.springframework.util.Assert;

public class SimpleServerEndpointProvider implements ServerEndpointProvider {

	final private String serverUrl;
	
	
	public SimpleServerEndpointProvider(String serverUrl) {
		super();
		Assert.hasText(serverUrl, "server url cant be blank.");
		this.serverUrl = StringUtils.appendEndWith(serverUrl, "/");
	}

	@Override
	public String getServerEndpoint() {
		return serverUrl;
	}
	
}
