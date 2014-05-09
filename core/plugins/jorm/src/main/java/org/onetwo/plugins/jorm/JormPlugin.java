package org.onetwo.plugins.jorm;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class JormPlugin extends AbstractContextPlugin<JormPlugin> {

	private static JormPlugin instance;
	
	
	public static JormPlugin getInstance() {
		return instance;
	}
	
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JormPluginContext.class);
	}


	public void setPluginInstance(JormPlugin plugin){
		instance = plugin;
	}

}
