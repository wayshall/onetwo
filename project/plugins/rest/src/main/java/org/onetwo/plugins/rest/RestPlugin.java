package org.onetwo.plugins.rest;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class RestPlugin extends AbstractJFishPlugin<RestPlugin> {

	private static RestPlugin instance;
	
	
	public static RestPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(RestContext.class);
	}

	public void setPluginInstance(RestPlugin plugin){
		instance = plugin;
	}

}
