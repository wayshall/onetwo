package org.onetwo.plugins.session;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;

public class SessionWebPlugin extends AbstractJFishPlugin<SessionWebPlugin> {

	private static SessionWebPlugin instance;
	
	
	public static SessionWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
//		annoClasses.add(SessionPluginWebContext.class);
	}


	public void setPluginInstance(SessionWebPlugin plugin){
		instance = plugin;
	}

}
