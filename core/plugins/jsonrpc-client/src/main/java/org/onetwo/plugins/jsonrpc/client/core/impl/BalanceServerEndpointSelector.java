package org.onetwo.plugins.jsonrpc.client.core.impl;

import java.util.List;

import org.javatuples.Pair;
import org.onetwo.common.jsonrpc.zk.ServerPathData;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class BalanceServerEndpointSelector implements ServerEndpointSelector {

	@Override
	public Pair<String, ServerPathData> findEndpoint(CuratorClient curatorClient, Class<?> interfaceClass, String providerPath) {
		List<String> childrenPath = curatorClient.getChildren(providerPath, true);
		ServerPathData serverPathData = null;
		String serverPath = null;
		for(String path : childrenPath){
			ServerPathData data = curatorClient.getData(path, ServerPathData.class);
			if(serverPathData==null){
				serverPathData = data;
				serverPath = path;
			}else if(data.getClientCount()<serverPathData.getClientCount()){
				serverPathData = data;
				serverPath = path;
			}
		}
		if(StringUtils.isBlank(serverPath)){
			return null;
		}else{
			return Pair.with(serverPath, serverPathData);
		}
	}

}
