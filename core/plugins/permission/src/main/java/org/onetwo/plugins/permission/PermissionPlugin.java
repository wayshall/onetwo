package org.onetwo.plugins.permission;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class PermissionPlugin extends AbstractJFishPlugin<PermissionPlugin> {

	private static PermissionPlugin instance;
	
	
	public static PermissionPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PermissionPluginContext.class);
	}


	public void setPluginInstance(PermissionPlugin plugin){
		instance = plugin;
	}

}
