package org.onetwo.plugins.jsonrpc.client;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;

public class JsonRpcClientWebPlugin extends AbstractJFishPlugin<JsonRpcClientWebPlugin> {

	private static JsonRpcClientWebPlugin instance;
	
	
	public static JsonRpcClientWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void setPluginInstance(JsonRpcClientWebPlugin plugin){
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
