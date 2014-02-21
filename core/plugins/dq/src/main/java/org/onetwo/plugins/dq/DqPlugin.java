package org.onetwo.plugins.dq;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class DqPlugin extends AbstractContextPlugin<DqPlugin> {

	private static DqPlugin instance;
	
	
	public static DqPlugin getInstance() {
		return instance;
	}
	
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(DqPluginContext.class);
	}


	public void setPluginInstance(DqPlugin plugin){
		instance = (DqPlugin)plugin;
	}

}
