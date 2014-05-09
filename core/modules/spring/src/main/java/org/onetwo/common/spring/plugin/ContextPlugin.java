package org.onetwo.common.spring.plugin;

import java.util.List;

public interface ContextPlugin {

	void init(ContextPluginMeta pluginMeta);
	
	void onJFishContextClasses(List<Class<?>> annoClasses);
	
//	<T> T getExtComponent(Class<T> extClasss);

}
