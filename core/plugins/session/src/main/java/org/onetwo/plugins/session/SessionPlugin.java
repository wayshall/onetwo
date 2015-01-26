package org.onetwo.plugins.session;

import java.util.List;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;
import org.onetwo.plugins.session.model.SessionPluginContext;



public class SessionPlugin extends ConfigurableContextPlugin<SessionPlugin, SessionPluginConfig> {

	private static SessionPlugin instance;
	
	
	public static SessionPlugin getInstance() {
		return instance;
	}
	

	public SessionPlugin() {
		super("/org.onetwo.plugins.sessionoplugin/", "org.onetwo.plugins.sessionoplugin-config");
	}



	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SessionPluginContext.class);
	}

	public void setPluginInstance(SessionPlugin plugin){
		instance = plugin;
	}

}
