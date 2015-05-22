package org.onetwo.plugins.rest;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;


public class RestWebPlugin extends AbstractJFishPlugin<RestWebPlugin> {

	private static RestWebPlugin instance;
	
	
	public static RestWebPlugin getInstance() {
		return instance;
	}

	public void setPluginInstance(RestWebPlugin plugin){
		instance = plugin;
	}
	
	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(final MvcContextConfigRegisterEvent event){
				event.registerConfigClasses(RestWebContext.class);
			}
			
		};
	}
}
