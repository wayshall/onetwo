package org.onetwo.plugins.jdoc;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class JDocPlugin extends AbstractJFishPlugin<JDocPlugin> {

	private static JDocPlugin instance;
	
	
	public static JDocPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JDocContext.class);
	}
	
	public void setPluginInstance(JDocPlugin plugin){
		instance = plugin;
	}

}
