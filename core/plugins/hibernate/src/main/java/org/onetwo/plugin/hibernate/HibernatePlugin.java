package org.onetwo.plugin.hibernate;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class HibernatePlugin extends AbstractContextPlugin<HibernatePlugin> {

	private static HibernatePlugin instance;
	
	
	public static HibernatePlugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(HibernatePluginContext.class);
	}



	@Override
	public void setPluginInstance(HibernatePlugin plugin){
		instance = (HibernatePlugin)plugin;
	}

}
