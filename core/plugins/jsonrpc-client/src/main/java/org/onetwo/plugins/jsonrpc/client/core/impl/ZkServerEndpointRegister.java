package org.onetwo.plugins.jsonrpc.client.core.impl;

import javax.annotation.Resource;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.onetwo.common.exception.BaseException;
import org.onetwo.plugins.jsonrpc.client.RpcClientPluginConfig;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientCreatedEvent;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientListener;
import org.onetwo.plugins.zkclient.core.Zkclienter;

public class ZkServerEndpointRegister implements JsonRpcClientListener{
	
	@Resource
	private Zkclienter Zkclienter;
	@Resource
	private RpcClientPluginConfig rpcClientPluginConfig;

	@Override
	public void onCreated(JsonRpcClientCreatedEvent event) {
		Class<?> interfaceClass = event.getInterfaceClass();
		String providerPath = rpcClientPluginConfig.getRpcServiceProviderNode(interfaceClass.getName());
		String consumerPath = rpcClientPluginConfig.getRpcServiceConsumerNode(interfaceClass.getName());
		
		Stat stat = Zkclienter.exists(consumerPath, false);
		if(stat==null){
			throw new BaseException("can not found the consumer node in zkserver, check it. interface: " + interfaceClass + ", consumer path: " + consumerPath);
		}
		
		String consumerAddressPath = rpcClientPluginConfig.getZkConsumerAddressNode(consumerPath);
		
		stat = Zkclienter.exists(consumerAddressPath, false);
		if(stat!=null){
			throw new BaseException("consumer address path has registered. interface: " + interfaceClass + ", consumerAddressPath: " + consumerAddressPath);
		}
		
//		Zkclienter.getZooKeeper().getChildren(providerPath, true);
		
//		Zkclienter.create(consumerAddressPath, data, CreateMode.EPHEMERAL);
	}
	
}
