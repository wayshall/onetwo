package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.javatuples.Pair;
import org.onetwo.common.jsonrpc.zk.ServerPathData;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public interface ServerEndpointSelector {
	
	public Pair<String, ServerPathData> findEndpoint(CuratorClient curatorClient, Class<?> interfaceClass, String providerPath);

}
