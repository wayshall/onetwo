package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class SecurityPlugin extends AbstractContextPlugin<SecurityPlugin> {

	private static SecurityPlugin instance;
	
	
	public static SecurityPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(SecurityPlugin plugin){
		instance = plugin;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
//		annoClasses.add(SecurityModelContext.class);
	}


}
