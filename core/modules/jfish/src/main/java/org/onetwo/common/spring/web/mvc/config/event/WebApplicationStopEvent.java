package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.springframework.web.context.WebApplicationContext;

public class WebApplicationStopEvent {
	final private JFishPluginManager jfishPluginManager;
	final private WebApplicationContext webApplicationContext;
	
	public WebApplicationStopEvent(JFishPluginManager jfishPluginManager,
			WebApplicationContext webApplicationContext) {
		super();
		this.jfishPluginManager = jfishPluginManager;
		this.webApplicationContext = webApplicationContext;
	}

	public JFishPluginManager getJFishPluginManager() {
		return jfishPluginManager;
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}
	
	

}
