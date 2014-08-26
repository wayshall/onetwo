package org.onetwo.plugins.task.client;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.plugins.task.entity.TaskBase;


public class TaskClientPlugin extends ConfigurableContextPlugin<TaskClientPlugin, TaskClientConfig> {

	public TaskClientPlugin() {
		super("/task/", "client", new TaskClientConfig());
	}


	private static TaskClientPlugin instance;
	
	
	public static TaskClientPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(TaskClientPlugin plugin){
		instance = plugin;
	}
	

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		if(isConfigExists()){
			annoClasses.add(TaskClientPluginContext.class);
		}
	}

	@Override
	public void registerEntityPackage(List<String> packages) {
		packages.add(TaskBase.class.getPackage().getName());
	}
	
	

}
