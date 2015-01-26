package org.onetwo.plugins.session;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.plugins.session.web.SessionPluginWebContext;



public class SessionPluginWebPlugin extends AbstractJFishPlugin<SessionPluginWebPlugin> {

	private static SessionPluginWebPlugin instance;
	
	
	public static SessionPluginWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SessionPluginWebContext.class);
	}


	public void setPluginInstance(SessionPluginWebPlugin plugin){
		instance = plugin;
	}

}
