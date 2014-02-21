package org.onetwo.plugins.jasper;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class JasperWebPlugin extends AbstractJFishPlugin<JasperWebPlugin> {

	private static JasperWebPlugin instance;
	
	
	public static JasperWebPlugin getInstance() {
		return instance;
	}

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JasperMvcContext.class);
	}


	public void setPluginInstance(JasperWebPlugin plugin){
		instance = plugin;
	}

}
