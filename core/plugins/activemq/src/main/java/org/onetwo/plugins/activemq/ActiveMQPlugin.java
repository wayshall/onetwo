package org.onetwo.plugins.activemq;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.plugins.activemq.model.PluginModelContext;



public class ActiveMQPlugin extends ConfigurableContextPlugin<ActiveMQPlugin, ActiveMQConfig> {

	public ActiveMQPlugin() {
		super("/plugins/activemq/", "activemq-config", true);
	}


	private static ActiveMQPlugin instance;
	
	
	public static ActiveMQPlugin getInstance() {
		return instance;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginModelContext.class);
	}


	public void setPluginInstance(ActiveMQPlugin plugin){
		instance = plugin;
	}

}
