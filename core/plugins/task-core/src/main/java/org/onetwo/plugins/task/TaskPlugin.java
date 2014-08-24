package org.onetwo.plugins.task;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class TaskPlugin extends AbstractContextPlugin<TaskPlugin> {

	private static TaskPlugin instance;
	
	
	public static TaskPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(TaskPlugin plugin){
		instance = plugin;
	}
	

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(TaskPluginContext.class);
	}


}
