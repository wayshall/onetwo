package org.onetwo.plugins.permission;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class PermissionPlugin extends AbstractContextPlugin<PermissionPlugin> {

	private static PermissionPlugin instance;
	
	
	public static PermissionPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PermissionPluginContext.class);
	}

	public void setPluginInstance(PermissionPlugin plugin){
		instance = plugin;
	}

}
