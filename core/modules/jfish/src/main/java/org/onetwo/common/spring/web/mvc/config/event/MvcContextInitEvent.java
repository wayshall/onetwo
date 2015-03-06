package org.onetwo.common.spring.web.mvc.config.event;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig;

public class MvcContextInitEvent {
	private final JFishMvcApplicationContext applicationContext;
	private final JFishMvcConfig mvcConfig;
	private final JFishPluginManager jfishPluginManager;
	public MvcContextInitEvent(JFishMvcApplicationContext applicationContext,
			JFishPluginManager jfishPluginManager,
			JFishMvcConfig mvcConfig) {
		super();
		this.applicationContext = applicationContext;
		this.mvcConfig = mvcConfig;
		this.jfishPluginManager = jfishPluginManager;
	}
	public JFishMvcApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public JFishMvcConfig getMvcConfig() {
		return mvcConfig;
	}
	public JFishPluginManager getJfishPluginManager() {
		return jfishPluginManager;
	}
	
	
}
