package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.web.mvc.config.event.JFishMvcEventBus;
import org.springframework.web.context.WebApplicationContext;

/****
 * jfish plugin manager interface
 * 扫描管理 JFishPlugin 接口的实现者
 * 基于web项目的插件接口，非web项目插件见{@linkplain ContextPluginManager}
 * @author way
 *
 */
public interface JFishWebMvcPluginManager {
	PluginNameParser PLUGINNAME_PARSER = new PluginNameParser();

	public String JFISH_PLUGIN_MANAGER_KEY = "org.onetwo.common.fish.plugin.JFishPluginManager";

	public PluginNameParser getPluginNameParser();
	
	/****
	 * called when jfish dispatcher servlet(spring mvc) initialize mvc context
	 * {@linkplain org.onetwo.common.spring.web.JFishDispatcher#initWebApplicationContext initWebApplicationContext}
	 * it will start JFishMvcApplicationContext
	 * @param appContext
	 */
	public void onWebApplicationContextStartup(final WebApplicationContext appContext);
	public void onWebApplicationContextStartupCompleted();
	
//	public JFishList<JFishPluginMeta> getPluginMetas();
	
	public JFishWebMvcPluginMeta getJFishPluginMetaOf(Class<?> objClass);
	
	public JFishWebMvcPluginMeta getJFishPluginMeta(String name);
	public List<JFishWebMvcPluginMeta> getPluginMetas();
	public List<ContextPlugin> getContextPlugins();
	public List<JFishPlugin> getJFishPlugins();
	
//	public List<Class<?>> getPluginContextClasses();

	/****
	 * called when jfish initialize mvc context {@linkplain org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext JFishMvcApplicationContext}
	 * @param contextClasses
	 */
//	public void registerPluginMvcContextClasses(List<Class<?>> contextClasses);
	
	public JFishMvcEventBus getMvcEventBus();

	public void destroy(final WebApplicationContext appContext);
	

}