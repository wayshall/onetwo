package org.onetwo.common.spring.plugin;

import java.util.List;

public interface ContextPlugin {

	void init(ContextPluginMeta pluginMeta, String appEnv);
	
	void onJFishContextClasses(List<Class<?>> annoClasses);
	
	public void registerEntityPackage(List<String> packages);
	
//	<T> T getExtComponent(Class<T> extClasss);

}
