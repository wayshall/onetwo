package org.onetwo.common.spring.plugin;

import org.onetwo.common.spring.plugin.event.JFishContextEventBus;
import org.onetwo.common.utils.list.JFishList;

/****
 * spring 上下文插件接口管理者
 * 扫描管理 ContextPlugin 接口的实现者
 * @author wayshall
 *
 */
public interface ContextPluginManager<T extends ContextPluginMeta> {

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
	/*public void registerPluginJFishContextClasses(List<Class<?>> contextClasses);
	

	public void registerEntityPackage(List<String> packages);*/
	public JFishContextEventBus getEventBus();
	
	public JFishList<T> getPluginMetas();
	public JFishList<ContextPlugin> getContextPlugins();
}
