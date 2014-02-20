package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.plugins.groovy.model.PluginModelContext;
import org.onetwo.plugins.groovy.web.PluginWebContext;



public class Plugin extends AbstractJFishPlugin<Plugin> {

	private static Plugin instance;
	
	
	public static Plugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginModelContext.class);
	}

	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginWebContext.class);
	}


	public void setPluginInstance(Plugin plugin){
		instance = plugin;
	}

}
