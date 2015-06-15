package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.javatuples.Pair;
import org.onetwo.common.jsonrpc.zk.ServerPathData;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class SimpleServerEndpointSelector implements ServerEndpointSelector {

	@Override
	public Pair<String, ServerPathData> findEndpoint(CuratorClient curatorClient, Class<?> interfaceClass, String providerPath) {
		String serverPath = curatorClient.findFirstChild(providerPath, true);
		if(StringUtils.isBlank(serverPath)){
			return null;
		}else{
			ServerPathData serverPathData = curatorClient.getData(serverPath, ServerPathData.class);
			return Pair.with(serverPath, serverPathData);
		}
	}

}
