package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;

/*******
 * 插件接口
 * 
 * @author weishao
 *
 */
public interface JFishPlugin extends ContextPlugin<JFishPluginMeta> {

	void onMvcContextClasses(List<Class<?>> annoClasses);
	
	JFishMvcConfigurerListener getJFishMvcConfigurerListener();
	
//	JFishContextConfigurerListener getJFishContextConfigurerListener();
	
	public JFishPluginMeta getPluginMeta();
	
	public boolean registerMvcResources();
	
	public PluginConfig getPluginConfig();
}
