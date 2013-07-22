package org.onetwo.plugins.jorm;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class JormPlugin extends AbstractJFishPlugin<JormPlugin> {

	private static JormPlugin instance;
	
	
	public static JormPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginContext.class);
	}


	public void setPluginInstance(JormPlugin plugin){
		instance = plugin;
	}

}
