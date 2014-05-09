package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;



public class GroovyWebPlugin extends AbstractJFishPlugin<GroovyWebPlugin> {

	private static GroovyWebPlugin instance;
	
	
	public static GroovyWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
//		annoClasses.add(PluginWebContext.class);
	}


	public void setPluginInstance(GroovyWebPlugin plugin){
		instance = plugin;
	}

}
