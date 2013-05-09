package org.onetwo.plugins.richmodel;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class RichModelPlugin extends AbstractJFishPlugin<RichModelPlugin> {

	private static RichModelPlugin instance;
	
	
	public static RichModelPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(RichModelContext.class);
	}

	public void setPluginInstance(RichModelPlugin plugin){
		instance = plugin;
	}

}
