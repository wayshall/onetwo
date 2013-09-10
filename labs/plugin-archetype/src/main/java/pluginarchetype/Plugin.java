package pluginarchetype;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;

import pluginarchetype.app.PluginAppContext;
import pluginarchetype.mvc.PluginMvcContext;


public class Plugin extends AbstractJFishPlugin<Plugin> {

	private static Plugin instance;
	
	
	public static Plugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginAppContext.class);
	}

	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginMvcContext.class);
	}


	public void setPluginInstance(Plugin plugin){
		instance = plugin;
	}

}
