package org.onetwo.plugins.permission;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class PermissionWebPlugin extends AbstractJFishPlugin<PermissionWebPlugin> {

	private static PermissionWebPlugin instance;
	
	
	public static PermissionWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PermissionMvcContext.class);
	}

	public void setPluginInstance(PermissionWebPlugin plugin){
		instance = plugin;
	}

}
