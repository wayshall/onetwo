package org.onetwo.plugins.jsonrpc;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;

public class JsonRpcWebPlugin extends AbstractJFishPlugin<JsonRpcWebPlugin> {

	private static JsonRpcWebPlugin instance;
	
	
	public static JsonRpcWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void setPluginInstance(JsonRpcWebPlugin plugin){
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
