package onetwo_plugin_project;

import java.util.List;

import onetwo_plugin_project.model.onetwo_plugin_projectContext;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;



public class onetwo_plugin_projectPlugin extends AbstractContextPlugin<onetwo_plugin_projectPlugin> {

	private static onetwo_plugin_projectPlugin instance;
	
	
	public static onetwo_plugin_projectPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(onetwo_plugin_projectContext.class);
	}

	public void setPluginInstance(onetwo_plugin_projectPlugin plugin){
		instance = plugin;
	}

}
