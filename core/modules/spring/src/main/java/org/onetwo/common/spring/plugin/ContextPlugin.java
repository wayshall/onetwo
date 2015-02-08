package org.onetwo.common.spring.plugin;

import org.onetwo.common.spring.plugin.event.JFishContextPluginListener;

/****
 * 插件类一般放在插件项目的根目录，以识别是否属于插件类
 * {@link org.onetwo.common.fish.plugin.JFishPluginMeta#isClassOfThisPlugin JFishPluginMeta#isClassOfThisPlugin} 
 * @author wayshall
 *
 */
public interface ContextPlugin {
	
//	ContextPlugin EMTPY_CONTEXT_PLUGIN = new EmptyContextPlugin();

	public boolean isEmptyPlugin();
	
	void init(ContextPluginMeta pluginMeta, String appEnv);
	
	JFishContextPluginListener getJFishContextPluginListener();
	
	/*void onJFishContextClasses(List<Class<?>> annoClasses);
	
	public void registerEntityPackage(List<String> packages);*/
	
//	<T> T getExtComponent(Class<T> extClasss);

}
