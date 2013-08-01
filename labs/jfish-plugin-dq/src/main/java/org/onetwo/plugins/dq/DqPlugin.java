package org.onetwo.plugins.dq;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class DqPlugin extends AbstractJFishPlugin<DqPlugin> {

	private static DqPlugin instance;
	
	
	public static DqPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(DqPluginContext.class);
	}


	public void setPluginInstance(DqPlugin plugin){
		instance = plugin;
	}

}
