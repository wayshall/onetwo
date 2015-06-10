package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;

public class WebApplicationStartupCompletedEvent {
	final private JFishWebMvcPluginManager jfishPluginManager;
//	final private WebApplicationContext webApplicationContext;
	
	public WebApplicationStartupCompletedEvent(JFishWebMvcPluginManager jfishPluginManager) {
		super();
		this.jfishPluginManager = jfishPluginManager;
	}

	public JFishWebMvcPluginManager getJFishPluginManager() {
		return jfishPluginManager;
	}

}
