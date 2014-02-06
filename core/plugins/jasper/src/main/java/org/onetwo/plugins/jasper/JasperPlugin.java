package org.onetwo.plugins.jasper;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class JasperPlugin extends AbstractJFishPlugin<JasperPlugin> {

	private static JasperPlugin instance;
	
	
	public static JasperPlugin getInstance() {
		return instance;
	}

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JasperPluginContext.class);
	}

	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JasperMvcContext.class);
	}


	public void setPluginInstance(JasperPlugin plugin){
		instance = plugin;
	}

}
