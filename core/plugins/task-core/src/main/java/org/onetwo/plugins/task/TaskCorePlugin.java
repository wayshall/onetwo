package org.onetwo.plugins.task;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;


public class TaskCorePlugin extends ConfigurableContextPlugin<TaskCorePlugin, TaskPluginConfig> {

	public TaskCorePlugin() {
		super("/plugins/task/", "task-config", new TaskPluginConfig());
	}
	private static TaskCorePlugin instance;
	
	
	public static TaskCorePlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(TaskCorePlugin plugin){
		instance = plugin;
	}
	

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(TaskPluginContext.class);
	}


}
