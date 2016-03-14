package org.onetwo.plugins.jsonrpc.client;

import org.apache.curator.utils.ZKPaths;
import org.onetwo.common.jsonrpc.plugin.AbstractRpcPluginConfig;
import org.onetwo.common.jsonrpc.utils.RpcUtils.ConfigValue;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.zkclient.utils.ZkUtils;

public class RpcClientPluginConfig extends AbstractRpcPluginConfig {
	public static final String RPC_CONSUMER_ADDRESS = "rpc.consumer.address";
	
	private String[] rpcClientPackages;
	private String rpcServerEndpoint;
	private boolean rpcClientScanable;
	private String rpcConsumerAddress;
	
	@Override
	protected void initConfig(JFishProperties config) {
		super.initConfig(config);
		
		rpcClientPackages = config.getStringArray("rpc.client.packages", ",");
		rpcClientScanable = !LangUtils.isEmpty(rpcClientPackages);
		rpcServerEndpoint = config.getProperty("rpc.server.endpoint");

		String address = config.getProperty(RPC_CONSUMER_ADDRESS, NetUtils.getLocalAddress());
		rpcConsumerAddress = parseAddress(address);
//		System.out.println("rpcServerEndpoint:"+rpcServerEndpoint);
	}

	public String[] getRpcClientPackages() {
		return rpcClientPackages;
	}

	public String getRpcServerEndpoint() {
		return rpcServerEndpoint;
	}

	public boolean isRpcClientScanable() {
		return rpcClientScanable;
	}

	public boolean isFindServerEndpointFromZk() {
		return rpcProvider==RpcProvider.ZK;
	}
	
	public String getRpcServiceConsumerNode(String servicePath){
		return servicePath + ConfigValue.CONSUMER_PATH;
	}
	public String getRpcServiceProviderNode(String servicePath){
		return servicePath + ConfigValue.PROVIDER_PATH;
	}
	
	public String getZkConsumerAddressNode(String consumerPath){
		String address = ZkUtils.encodePath(rpcConsumerAddress);
		address = ZKPaths.makePath(consumerPath, address);
		return address;
	}


}
