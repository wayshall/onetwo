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
		
		//创建service节点
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(servicePath, "".getBytes(), CreateMode.PERSISTENT, true);
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(consumerPath, "".getBytes(), CreateMode.PERSISTENT, true);
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(providerPath, "".getBytes(), CreateMode.PERSISTENT, true);
		
		//以jsonrpc server的地址作为节点名称
		String serverAddressNode = rpcServerPluginConfig.getZkProviderAddressPath(providerPath);
		String data = rpcServerPluginConfig.getProviderAddress();
		rpcServerZkClienter.getCuratorClient().creatingParentsIfNeeded(serverAddressNode, data, CreateMode.EPHEMERAL, false);
		logger.info("register to zkserver for service node: {} ", serverAddressNode);
	}
	
	

}
