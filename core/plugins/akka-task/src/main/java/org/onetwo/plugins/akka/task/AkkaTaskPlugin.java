package org.onetwo.plugins.akka.task;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;



public class AkkaTaskPlugin extends ConfigurableContextPlugin<AkkaTaskPlugin, AkkaTaskConfig> {

	private static AkkaTaskPlugin instance;
	
	
	public static AkkaTaskPlugin getInstance() {
		return instance;
	}
	

	public AkkaTaskPlugin() {
		super("/akka-task/", "akka-task-config");
	}



	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(AkkaTaskPluginContext.class);
	}

	public void setPluginInstance(AkkaTaskPlugin plugin){
		instance = plugin;
	}

}
