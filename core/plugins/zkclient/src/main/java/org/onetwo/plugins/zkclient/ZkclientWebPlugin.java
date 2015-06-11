package org.onetwo.plugins.zkclient;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;

public class ZkclientWebPlugin extends AbstractJFishPlugin<ZkclientWebPlugin> {

	private static ZkclientWebPlugin instance;
	
	
	public static ZkclientWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void setPluginInstance(ZkclientWebPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(final MvcContextConfigRegisterEvent event){
				if(ZkclientPlugin.getInstance().isConfigExists()){
//					event.registerConfigClasses(ZkclientWebContext.class);
				}
			}
		};
	}

}
