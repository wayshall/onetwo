package org.onetwo.plugins.task.client;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.task.entity.TaskBase;


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

	@Override
	public void registerEntityPackage(List<String> packages) {
		packages.add(TaskBase.class.getPackage().getName());
	}
	
	

}
