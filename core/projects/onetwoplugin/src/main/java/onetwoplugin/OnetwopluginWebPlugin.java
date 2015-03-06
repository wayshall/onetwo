package onetwoplugin;

import java.util.List;

import onetwoplugin.web.OnetwopluginWebContext;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;



public class OnetwopluginWebPlugin extends AbstractJFishPlugin<OnetwopluginWebPlugin> {

	private static OnetwopluginWebPlugin instance;
	
	
	public static OnetwopluginWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(OnetwopluginWebContext.class);
	}


	public void setPluginInstance(OnetwopluginWebPlugin plugin){
		instance = plugin;
	}

}
