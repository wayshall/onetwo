package pluginarchetype;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;

import pluginarchetype.web.PluginWebContext;


public class WebPlugin extends AbstractJFishPlugin<WebPlugin> {

	private static WebPlugin instance;
	
	
	public static WebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(PluginWebContext.class);
	}


	public void setPluginInstance(WebPlugin plugin){
		instance = plugin;
	}

}
