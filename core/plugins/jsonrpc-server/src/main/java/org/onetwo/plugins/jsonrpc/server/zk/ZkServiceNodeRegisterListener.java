package org.onetwo.plugins.jsonrpc.server.zk;

import javax.annotation.Resource;

import org.apache.zookeeper.CreateMode;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.plugins.jsonrpc.server.RpcServerPluginConfig;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcSerivceFoundEvent;
import org.onetwo.plugins.jsonrpc.server.core.JsonRpcSerivceListener;
import org.slf4j.Logger;

public class ZkServiceNodeRegisterListener implements JsonRpcSerivceListener {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Resource
	private RpcServerZkClienter rpcServerZkClienter;
//	private Zkclienter zkclienter;
	
	@Resource
	private RpcServerPluginConfig rpcServerPluginConfig;

	@Override
	public void onFinded(JsonRpcSerivceFoundEvent event) {
		String servicePath = event.getInterfaceName();
		String providerPath = rpcServerPluginConfig.getRpcServiceProviderNode(servicePath);
		String consumerPath = rpcServerPluginConfig.getRpcServiceConsumerNode(servicePath);
		
		/*Stat stat = zkclienter.exists(servicePath, true);
		if(stat==null){
			servicePath = zkclienter.createPersistent(servicePath);
			providerPath = zkclienter.createPersistent(providerPath);
			consumerPath = zkclienter.createPersistent(consumerPath);
		}else{
			stat = zkclienter.existsOrCreate(providerPath, true, (createPath)->zkclienter.createPersistent(createPath));
			if(stat==null){
				providerPath = zkclienter.createPersistent(providerPath);
			}
			stat = zkclienter.existsOrCreate(consumerPath, true, (createPath)->zkclienter.createPersistent(createPath));
		}
		String addressNode = rpcServerPluginConfig.getZkProviderAddressNode(providerPath);
		addressNode = zkclienter.createEphemeral(addressNode);
		logger.info("service provider address [{}] has register!", addressNode);*/
//		rpcServerZkClienter.creatingParentsIfNeeded(providerPath);
		
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(consumerPath, "".getBytes(), CreateMode.PERSISTENT, true);
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(servicePath, "".getBytes(), CreateMode.PERSISTENT, true);
		
		String serverAddressNode = rpcServerPluginConfig.getZkProviderAddressNode(providerPath);
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(serverAddressNode, "".getBytes(), CreateMode.EPHEMERAL, false);
		logger.info("register to zkserver for service node: {} ", serverAddressNode);
	}
	
	

}
