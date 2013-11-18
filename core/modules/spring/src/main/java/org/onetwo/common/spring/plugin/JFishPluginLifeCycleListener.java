package org.onetwo.common.spring.plugin;

import org.springframework.web.context.WebApplicationContext;

/***
 * 插件生命周期接口
 * 
 * @author weishao
 *
 */
public interface JFishPluginLifeCycleListener<T extends ContextPluginMeta<? extends ContextPlugin<?>>> {

	void init(T pluginMeta);
	void onStartWebAppConext(WebApplicationContext appContext);
	void onStopWebAppConext();
}
