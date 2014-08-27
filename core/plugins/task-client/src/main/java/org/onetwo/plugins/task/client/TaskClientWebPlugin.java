package org.onetwo.plugins.task.client;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;


public class TaskClientWebPlugin extends AbstractJFishPlugin<TaskClientWebPlugin> {

	private static TaskClientWebPlugin instance;
	
	
	public static TaskClientWebPlugin getInstance() {
		return instance;
	}


	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(TaskClientWebPluginContext.class);
	}
	
	public void setPluginInstance(TaskClientWebPlugin plugin){
		instance = (TaskClientWebPlugin)plugin;
	}

	@Override
	public boolean registerMvcResources() {
		return true;
	}

}
