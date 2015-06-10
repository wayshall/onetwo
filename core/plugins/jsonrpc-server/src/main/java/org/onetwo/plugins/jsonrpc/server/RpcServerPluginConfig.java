package org.onetwo.plugins.jsonrpc.server;

import org.onetwo.common.jsonrpc.utils.ZkUtils.ConfigValue;
import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.util.Assert;

public class RpcServerPluginConfig extends AbstractLoadingConfig {
	
	public static final String RPC_PROVIDER_ADDRESS = "rpc.provider.address";
	
	private String[] rpcSerivcePackages;
	private String providerAddress;
	private boolean registerToZk;
	
	@Override
	protected void initConfig(JFishProperties config) {
		this.rpcSerivcePackages = config.getStringArray("rpc.serivce.packages", ",");
//		Assert.notEmpty(this.rpcSerivcePackages, "no [rpc.serivce.packages] config!");
		String address = config.getProperty(RPC_PROVIDER_ADDRESS, "");
		if(address.startsWith(":")){
			//只写端口
			this.providerAddress = NetUtils.getLocalAddress() + address;
		}else{
			this.providerAddress = address;
		}
		//如果有配置RPC_PROVIDER_ADDRESS，则注册到zkserver
		this.registerToZk = StringUtils.isNotBlank(providerAddress);
	}

	public String[] getRpcSerivcePackages() {
		return rpcSerivcePackages;
	}

	public String getProviderAddress() {
		return providerAddress;
	}

	public String getZkProviderAddressNode(String providerPath) {
		Assert.hasText(providerAddress, "config["+RPC_PROVIDER_ADDRESS+"] can't be empty!");
		return providerPath + StringUtils.appendStartWithSlash(providerAddress);
	}
	
	public String getRpcServiceProviderNode(String servicePath){
		return servicePath + ConfigValue.PROVIDER_PATH;
	}
	
	public String getRpcServiceConsumerNode(String servicePath){
		return servicePath + ConfigValue.CONSUMER_PATH;
	}

	public boolean isRegisterToZk() {
		return registerToZk;
	}

}
