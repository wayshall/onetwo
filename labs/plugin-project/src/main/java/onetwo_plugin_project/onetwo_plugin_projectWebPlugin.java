package onetwo_plugin_project;

import java.util.List;

import onetwo_plugin_project.web.onetwo_plugin_projectWebContext;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;



public class onetwo_plugin_projectWebPlugin extends AbstractJFishPlugin<onetwo_plugin_projectWebPlugin> {

	private static onetwo_plugin_projectWebPlugin instance;
	
	
	public static onetwo_plugin_projectWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(onetwo_plugin_projectWebContext.class);
	}


	public void setPluginInstance(onetwo_plugin_projectWebPlugin plugin){
		instance = plugin;
	}

}
