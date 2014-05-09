package pluginarchetype;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;

import pluginarchetype.model.PluginModelContext;


public class Plugin extends AbstractContextPlugin<Plugin> {

	private static Plugin instance;
	
	
	public static Plugin getInstance() {
		return instance;
	}
	
	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginModelContext.class);
	}

	public void setPluginInstance(Plugin plugin){
		instance = plugin;
	}

}
