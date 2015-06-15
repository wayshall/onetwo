package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.zk.ServerPathData;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jsonrpc.client.core.ServerEndpointProvider;
import org.onetwo.plugins.zkclient.curator.CuratorClient;
import org.slf4j.Logger;

public class ZkPathServerEndpointProvider implements ServerEndpointProvider, PathChildrenCacheListener {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private CuratorClient curatorClient;
	private Class<?> interfaceClass;
	private String providerPath;
	private String serverPath;
	private String consumerAddressPath;
	private ServerPathData serverPathData;
	
//	private PathChildrenCache childrenCache;
	private volatile boolean connected;
	
	public ZkPathServerEndpointProvider(CuratorClient curatorClient, Class<?> interfaceClass,
			String providerPath, String consumerAddressPath) {
		super();
		this.curatorClient = curatorClient;
		this.providerPath = curatorClient.getActualNodePath(providerPath);
		this.consumerAddressPath = consumerAddressPath;
		this.interfaceClass = interfaceClass;
		
		serverPath = curatorClient.findFirstChild(providerPath, true);
		if(StringUtils.isBlank(serverPath)){
//			throw new BaseException("no rpc service provider found for: " + interfaceClass);
			connected = false;
			logger.info("build client[{}] waiting for server endpoint : {}", this.interfaceClass, serverPath);
		}else{
			serverPathData = curatorClient.getData(serverPath, ServerPathData.class);
			serverPathData.increase(1);
			logger.info("build client[{}] with server endpoint : {}", this.interfaceClass, serverPath);
			this.connected = true;
		}
		curatorClient.creatingParentsIfNeeded(this.consumerAddressPath, serverPathData, CreateMode.EPHEMERAL, false);
		curatorClient.addPathChildrenListener(this.providerPath, this);
	}

	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		logger.info("receive zkserver event, providerPath: {} event: {}", providerPath, event.getType());
		Type eventType = event.getType();
		String childPath = event.getData().getPath();
		switch (eventType) {
			case CHILD_ADDED:
				if(!connected){
					serverPath = childPath;
					serverPathData = curatorClient.getData(serverPath, ServerPathData.class);
					connected = true;
					
					serverPathData.increase(1);
					curatorClient.creatingParentsIfNeeded(this.consumerAddressPath, serverPathData, CreateMode.EPHEMERAL, true);
					
					logger.info("server provider has online : " + childPath);
				}
				break;
				
			case CHILD_REMOVED:
				if(childPath.equals(serverPath)){
					connected = false;
					logger.info("server provider has offline : " + childPath);
				}
				break;
	
			default:
				logger.info("server provider recive event, type:{}, path:{}", eventType, childPath);
				break;
		}
	}

	@Override
	public String getServerEndpoint() {
		if(!connected){
			throw new JsonRpcException(JsonRpcError.SERVER_OFFLINE, "server provider has offline ");
		}
		return serverPathData.getServerUrl();
	}

	public CuratorClient getCuratorClient() {
		return curatorClient;
	}

	public String getProviderPath() {
		return providerPath;
	}
	
	

}
