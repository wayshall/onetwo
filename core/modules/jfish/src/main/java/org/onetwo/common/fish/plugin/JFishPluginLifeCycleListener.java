package org.onetwo.common.fish.plugin;

import org.springframework.web.context.WebApplicationContext;

/***
 * 插件生命周期接口
 * 
 * @author weishao
 *
 */
public interface JFishPluginLifeCycleListener {

	void init(JFishPluginMeta pluginMeta);
	void onStartWebAppConext(WebApplicationContext appContext);
	void onStopWebAppConext();
}
