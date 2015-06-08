package org.onetwo.plugins.jsonrpc.client;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;

public class RpcClientWebPlugin extends AbstractJFishPlugin<RpcClientWebPlugin> {

	private static RpcClientWebPlugin instance;
	
	
	public static RpcClientWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void setPluginInstance(RpcClientWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(final MvcContextConfigRegisterEvent event){
			}
		};
	}

}
