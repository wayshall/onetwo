package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.groovy.model.PluginModelContext;



public class GroovyPlugin extends AbstractContextPlugin<GroovyPlugin> {

	private static GroovyPlugin instance;
	
	
	public static GroovyPlugin getInstance() {
		return instance;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginModelContext.class);
	}

	public void setPluginInstance(GroovyPlugin plugin){
		instance = plugin;
	}

}
