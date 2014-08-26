package org.onetwo.common.spring.plugin;

import java.util.List;

/****
 * 插件类一般放在插件项目的根目录，以识别是否属于插件类
 * {@link org.onetwo.common.fish.plugin.JFishPluginMeta#isClassOfThisPlugin JFishPluginMeta#isClassOfThisPlugin} 
 * @author wayshall
 *
 */
public interface ContextPlugin {

	void init(ContextPluginMeta pluginMeta, String appEnv);
	
	void onJFishContextClasses(List<Class<?>> annoClasses);
	
	public void registerEntityPackage(List<String> packages);
	
//	<T> T getExtComponent(Class<T> extClasss);

}
