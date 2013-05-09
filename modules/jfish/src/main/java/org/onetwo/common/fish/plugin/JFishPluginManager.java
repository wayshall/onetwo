package org.onetwo.common.fish.plugin;

import java.util.List;

import org.springframework.web.context.WebApplicationContext;

public interface JFishPluginManager {

	public String JFISH_PLUGIN_MANAGER_KEY = "org.onetwo.common.fish.plugin.JFishPluginManager";

	public void scanPlugins();
	
	public void onInitWebApplicationContext(final WebApplicationContext appContext);
	
//	public JFishList<JFishPluginMeta> getPluginMetas();
	
	public JFishPluginMeta getJFishPluginMetaOf(Class<?> objClass);
	
	public JFishPluginMeta getJFishPluginMeta(String name);
	
//	public List<Class<?>> getPluginContextClasses();

	public void registerPluginMvcContextClasses(List<Class<?>> contextClasses);
	public void registerPluginJFishContextClasses(List<Class<?>> contextClasses);
	
	public void destroy();

}