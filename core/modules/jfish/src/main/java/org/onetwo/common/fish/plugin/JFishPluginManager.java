package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.web.mvc.config.event.JFishMvcEventBus;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.web.context.WebApplicationContext;

/****
 * jfish plugin manager interface
 * 扫描管理 JFishPlugin 接口的实现者
 * 基于web项目的插件接口，非web项目插件见{@linkplain ContextPluginManager}
 * @author way
 *
 */
public interface JFishPluginManager {

	public String JFISH_PLUGIN_MANAGER_KEY = "org.onetwo.common.fish.plugin.JFishPluginManager";

	public PluginNameParser getPluginNameParser();
	
	/****
	 * called when jfish dispatcher servlet(spring mvc) initialize mvc context
	 * {@linkplain org.onetwo.common.spring.web.JFishDispatcher#initWebApplicationContext initWebApplicationContext}
	 * it will start JFishMvcApplicationContext
	 * @param appContext
	 */
	public void onInitWebApplicationContext(final WebApplicationContext appContext);
	
//	public JFishList<JFishPluginMeta> getPluginMetas();
	
	public JFishPluginMeta getJFishPluginMetaOf(Class<?> objClass);
	
	public JFishPluginMeta getJFishPluginMeta(String name);
	public JFishList<JFishPluginMeta> getPluginMetas();
	public JFishList<ContextPlugin> getContextPlugins();
	public JFishList<JFishPlugin> getJFishPlugins();
	
//	public List<Class<?>> getPluginContextClasses();

	/****
	 * called when jfish initialize mvc context {@linkplain org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext JFishMvcApplicationContext}
	 * @param contextClasses
	 */
//	public void registerPluginMvcContextClasses(List<Class<?>> contextClasses);
	
	public JFishMvcEventBus getMvcEventBus();

	public void destroy();
	

}