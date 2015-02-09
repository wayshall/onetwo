package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;

/*******
 * 插件接口
 * 
 * @author weishao
 *
 */
public interface JFishPlugin extends JFishPluginLifeCycleListener{
//	JFishPlugin EMPTY_JFISH_PLUGIN = new JFishPluginAdapter();
	
	void init(JFishPluginMeta pluginMeta);

//	void onMvcContextClasses(List<Class<?>> annoClasses);
	
	JFishMvcConfigurerListener getJFishMvcConfigurerListener();
	
//	JFishContextConfigurerListener getJFishContextConfigurerListener();
	
	public JFishPluginMeta getPluginMeta();
	
	public boolean registerMvcResources();
	
	public PluginConfig getPluginConfig();
	

	public boolean isEmptyPlugin();
}
