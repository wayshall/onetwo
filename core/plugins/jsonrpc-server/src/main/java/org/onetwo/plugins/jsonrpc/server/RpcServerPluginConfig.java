package org.onetwo.plugins.jsonrpc.server;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.util.Assert;

public class RpcServerPluginConfig extends AbstractLoadingConfig {
	
	private String[] rpcSerivcePackages;
	
	@Override
	protected void initConfig(JFishProperties config) {
		this.rpcSerivcePackages = config.getStringArray("rpc.serivce.packages", ",");
//		Assert.notEmpty(this.rpcSerivcePackages, "no [rpc.serivce.packages] config!");
	}

	public String[] getRpcSerivcePackages() {
		return rpcSerivcePackages;
	}
	

}
