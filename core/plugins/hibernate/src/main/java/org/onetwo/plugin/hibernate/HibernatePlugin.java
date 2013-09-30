package org.onetwo.plugin.hibernate;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class HibernatePlugin extends AbstractJFishPlugin<HibernatePlugin> {

	private static HibernatePlugin instance;
	
	
	public static HibernatePlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(HibernatePluginContext.class);
	}


	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
//		annoClasses.add(HibernateMvcContext.class);
	}


	public void setPluginInstance(HibernatePlugin plugin){
		instance = plugin;
	}

}
