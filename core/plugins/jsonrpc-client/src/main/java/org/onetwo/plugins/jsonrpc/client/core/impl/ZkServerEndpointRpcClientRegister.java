package org.onetwo.plugins.jsonrpc.client.core.impl;

import org.apache.zookeeper.data.Stat;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jsonrpc.client.RpcClientPluginConfig;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientCreatedEvent;
import org.onetwo.plugins.jsonrpc.client.core.JsonRpcClientListener;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class ZkServerEndpointRpcClientRegister implements JsonRpcClientListener{

	
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private CuratorClient curatorClient;
	
	private RpcClientPluginConfig rpcClientPluginConfig;

	@Override
	public void onCreated(JsonRpcClientCreatedEvent event) {
		Class<?> interfaceClass = event.getInterfaceClass();
		String providerPath = rpcClientPluginConfig.getRpcServiceProviderNode(interfaceClass.getName());
		String consumerPath = rpcClientPluginConfig.getRpcServiceConsumerNode(interfaceClass.getName());
		
//		Stat stat = Zkclienter.exists(consumerPath, false);
		Stat stat = curatorClient.checkExists(providerPath);
		if(stat==null){
			throw new BaseException("can not found the provider node in zkserver, check it. interface: " + interfaceClass + ", providerPath: " + consumerPath);
		}
		
//		stat = Zkclienter.exists(consumerAddressPath, false);
		stat = curatorClient.checkExists(consumerPath);
		if(stat==null){
			throw new BaseException("can not found the consumer node in zkserver, check it. interface: " + interfaceClass + ", consumerPath: " + consumerPath);
		}

		
		String consumerAddressPath = rpcClientPluginConfig.getZkConsumerAddressNode(consumerPath);
		ZkPathServerEndpointProvider provider = new ZkPathServerEndpointProvider(curatorClient, interfaceClass, providerPath, consumerAddressPath);
		
		Object clientObj = event.getRpcCactory().create(provider, interfaceClass);
		String beanName = StringUtils.uncapitalize(interfaceClass.getSimpleName());
		event.registerClientBean(beanName, clientObj);
		
	}
	

	public void setCuratorClient(CuratorClient curatorClient) {
		this.curatorClient = curatorClient;
	}

	public void setRpcClientPluginConfig(RpcClientPluginConfig rpcClientPluginConfig) {
		this.rpcClientPluginConfig = rpcClientPluginConfig;
	}
	
}
