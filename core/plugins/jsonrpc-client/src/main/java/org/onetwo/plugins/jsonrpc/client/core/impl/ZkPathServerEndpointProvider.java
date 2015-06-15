package org.onetwo.plugins.jsonrpc.client.core.impl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.javatuples.Pair;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.zk.ServerPathData;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
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
	
	private ServerEndpointSelector serverEndpointSelector = new BalanceServerEndpointSelector();//new SimpleServerEndpointSelector();
	
	private ReadWriteLock rwlock = new ReentrantReadWriteLock();
	
	public ZkPathServerEndpointProvider(CuratorClient curatorClient, Class<?> interfaceClass,
			String providerPath, String consumerAddressPath) {
		super();
		this.curatorClient = curatorClient;
		this.providerPath = curatorClient.getActualNodePath(providerPath);
		this.consumerAddressPath = consumerAddressPath;
		this.interfaceClass = interfaceClass;
		
//		serverPath = curatorClient.findFirstChild(providerPath, true);
		Pair<String, ServerPathData> serverPathInfo = serverEndpointSelector.findEndpoint(curatorClient, interfaceClass, providerPath);
		if(serverPathInfo==null){
//			throw new BaseException("no rpc service provider found for: " + interfaceClass);
			this.connected = false;
			logger.info("build client[{}] waiting for server endpoint : {}", this.interfaceClass, serverPath);
			curatorClient.creatingParentsIfNeeded(this.consumerAddressPath, null, CreateMode.EPHEMERAL, false);
			
		}else{
			/*serverPathData = curatorClient.getData(serverPath, ServerPathData.class);
			serverPathData.increase(1);
			logger.info("build client[{}] with server endpoint : {}", this.interfaceClass, serverPath);*/
			this.getAndUpdateServerPathData(serverPathInfo, false);
			this.connected = true;
		}
		curatorClient.addPathChildrenListener(this.providerPath, this);
	}
	
	private void getAndUpdateServerPathData(Pair<String, ServerPathData> serverPathInfo, boolean checkBeforeCreate){
		this.serverPath = serverPathInfo.getValue0();
		this.serverPathData = serverPathInfo.getValue1();
		this.serverPathData.increase(1);;
		curatorClient.creatingParentsIfNeeded(this.consumerAddressPath, serverPathData, CreateMode.EPHEMERAL, checkBeforeCreate);
		logger.info("build client[{}] with server endpoint : {}", this.interfaceClass, serverPath);
	}

	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		logger.info("receive zkserver event, providerPath: {} event: {}", providerPath, event.getType());
		Type eventType = event.getType();
		String childPath = event.getData().getPath();
		switch (eventType) {
			case CHILD_ADDED:
				onAdded(childPath);
				break;
				
			case CHILD_REMOVED:
				onRemoved(childPath);
				break;
	
			default:
				logger.info("server provider recive event, type:{}, path:{}", eventType, childPath);
				break;
		}
	}
	

	private void onAdded(String childPath){
		if(!connected){
			Pair<String, ServerPathData> serverPathInfo = serverEndpointSelector.findEndpoint(curatorClient, interfaceClass, providerPath);
			LangUtils.lockAction(rwlock.writeLock(), ()->{
				if(serverPathInfo!=null){
					this.getAndUpdateServerPathData(serverPathInfo, true);
					logger.info("server provider has online : " + childPath);
					setConnected(true);
				}
			});
		}
	}
	
	private void onRemoved(String childPath){
		if(childPath.equals(serverPath)){
			logger.info("server provider has offline : " + childPath);
			Pair<String, ServerPathData> serverPathInfo = serverEndpointSelector.findEndpoint(curatorClient, interfaceClass, providerPath);
			LangUtils.lockAction(rwlock.writeLock(), ()->{
				if(serverPathInfo!=null){
					this.getAndUpdateServerPathData(serverPathInfo, true);
					logger.info("server provider has online : " + childPath);
					setConnected(true);
				}else{
					setConnected(false);
				}
			});
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

	private void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	

}
