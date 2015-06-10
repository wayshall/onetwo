package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.springframework.web.context.WebApplicationContext;

public class WebApplicationStopEvent {
	final private JFishWebMvcPluginManager jfishPluginManager;
	final private WebApplicationContext webApplicationContext;
	
	public WebApplicationStopEvent(JFishWebMvcPluginManager jfishPluginManager,
			WebApplicationContext webApplicationContext) {
		super();
		this.jfishPluginManager = jfishPluginManager;
		this.webApplicationContext = webApplicationContext;
	}

	public JFishWebMvcPluginManager getJFishPluginManager() {
		return jfishPluginManager;
	}

	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}
	
	

}
