package org.onetwo.plugins.richmodel;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class RichModelPlugin extends AbstractContextPlugin<RichModelPlugin>{

	private static RichModelPlugin instance;
	
	
	public static RichModelPlugin getInstance() {
		return instance;
	}
	

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(RichModelContext.class);
	}

	public void setPluginInstance(RichModelPlugin plugin){
		instance = plugin;
	}

}
