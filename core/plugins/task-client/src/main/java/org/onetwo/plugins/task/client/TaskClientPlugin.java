package org.onetwo.plugins.task.client;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class TaskClientPlugin extends AbstractContextPlugin<TaskClientPlugin> {

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
		annoClasses.add(TaskClientPluginContext.class);
	}


}
