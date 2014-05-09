package org.onetwo.plugins.jasper;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class JasperPlugin extends AbstractContextPlugin<JasperPlugin> {

	private static JasperPlugin instance;
	
	
	public static JasperPlugin getInstance() {
		return instance;
	}


	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(JasperPluginContext.class);
	}

	public void setPluginInstance(JasperPlugin plugin){
		instance = plugin;
	}

}
