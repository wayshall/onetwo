package org.onetwo.plugins.jsonrpc.server.zk;

import javax.annotation.Resource;

import org.onetwo.plugins.jsonrpc.server.RpcServerPluginConfig;
import org.onetwo.plugins.zkclient.curator.CuratorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkContext {
	
	@Resource
	private RpcServerPluginConfig rpcServerPluginConfig;
	
	@Resource
	private CuratorClient curatorClient;

	@Bean
	public ZkServiceNodeRegisterListener zkRegisterListener(){
		return new ZkServiceNodeRegisterListener();
	}
	
	@Bean
	public RpcServerZkClienter rpcServerZkClienter(){
		return new RpcServerZkClienter(curatorClient);
	}
}