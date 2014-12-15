package org.onetwo.plugins.groovy;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.plugins.groovy.model.GroovyPluginModelContext;



public class GroovyPlugin extends ConfigurableContextPlugin<GroovyPlugin, GroovyPluginConfig> {

	public GroovyPlugin() {
		super("/plugins/groovy/", "groovy-config", true);
	}

	private static GroovyPlugin instance;
	
	
	public static GroovyPlugin getInstance() {
		return instance;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(GroovyPluginModelContext.class);
	}

	public void setPluginInstance(GroovyPlugin plugin){
		instance = plugin;
	}

}
