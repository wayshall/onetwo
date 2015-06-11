package org.onetwo.plugins.jsonrpc.server;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jsonrpc.plugin.AbstractRpcPluginConfig;
import org.onetwo.common.jsonrpc.utils.RpcUtils.ConfigValue;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.plugins.zkclient.utils.ZkUtils;
import org.springframework.util.Assert;

public class RpcServerPluginConfig extends AbstractRpcPluginConfig {
	
	public static final String RPC_PROVIDER_ADDRESS = "rpc.provider.address";
	
	private String[] rpcSerivcePackages;
	private String providerAddress;
	
	@Override
	protected void initConfig(JFishProperties config) {
		super.initConfig(config);
		this.rpcSerivcePackages = config.getStringArray("rpc.serivce.packages", ",");
		String address = config.getProperty(RPC_PROVIDER_ADDRESS, "");
		providerAddress = parseAddress(address);
		
		//如果是发布到zkserver，需要配置发布到zkserver的发布地址providerAddress
//		this.rpcProvider = StringUtils.isBlank(providerAddress)?RpcProvider.DIRECT:RpcProvider.ZK;
		if(isRegisterToZk() && StringUtils.isBlank(providerAddress)){
			throw new BaseException("config ["+RPC_PROVIDER_ADDRESS+"] which will register to the zkserver can not be blank.");
		}
	}

	public String[] getRpcSerivcePackages() {
		return rpcSerivcePackages;
	}

	public String getProviderAddress() {
		return providerAddress;
	}

	public String getZkProviderAddressNode(String providerPath) {
		Assert.hasText(providerAddress, "config["+RPC_PROVIDER_ADDRESS+"] can't be empty!");
		String path = providerPath + StringUtils.appendStartWithSlash(providerAddress);
		return path;
	}
	
	public String getRpcServiceProviderNode(String servicePath){
		return servicePath + ConfigValue.PROVIDER_PATH;
	}
	
	public String getRpcServiceConsumerNode(String servicePath){
		return servicePath + ConfigValue.CONSUMER_PATH;
	}

	public boolean isRegisterToZk() {
		return rpcProvider==RpcProvider.ZK;
	}
	
}
