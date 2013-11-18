package org.onetwo.common.spring.plugin;

import java.util.List;

public interface ContextPluginManager {

	/****
	 * scan plugins on webapp application start 
	 * {@linkplain org.onetwo.common.fish.web.JFishWebApplicationContext JFishWebApplicationContext},
	 * it common start by web context listener.
	 */
	public void scanPlugins();

	/***
	 * called when jfish instance a {@linkplain org.onetwo.common.fish.web.JFishWebApplicationContext JFishWebApplicationContext} .
	 * @param contextClasses
	 */
	public void registerPluginJFishContextClasses(List<Class<?>> contextClasses);
	
}
