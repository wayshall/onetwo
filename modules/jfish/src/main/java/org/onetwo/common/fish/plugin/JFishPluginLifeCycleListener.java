package org.onetwo.common.fish.plugin;

import org.springframework.web.context.WebApplicationContext;

public interface JFishPluginLifeCycleListener {

	void init(JFishPluginMeta pluginMeta);
	void onStartWebAppConext(WebApplicationContext appContext);
	void onStopWebAppConext();
}
