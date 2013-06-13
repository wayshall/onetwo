package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;

public interface JFishPlugin extends JFishPluginLifeCycleListener {

	void onMvcContextClasses(List<Class<?>> annoClasses);
	void onJFishContextClasses(List<Class<?>> annoClasses);
	
	JFishMvcConfigurerListener getJFishMvcConfigurerListener();
	
//	JFishContextConfigurerListener getJFishContextConfigurerListener();
	
	public JFishPluginMeta getPluginMeta();
}
