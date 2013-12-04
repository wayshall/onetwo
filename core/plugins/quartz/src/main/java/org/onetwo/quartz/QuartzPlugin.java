package org.onetwo.quartz;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.common.spring.plugin.DefaultContextPluginMeta;


public class QuartzPlugin extends AbstractContextPlugin<QuartzPlugin, DefaultContextPluginMeta<QuartzPlugin>> {

	private static QuartzPlugin instance;
	
	
	public static QuartzPlugin getInstance() {
		return instance;
	}

	public void setPluginInstance(QuartzPlugin plugin){
		instance = plugin;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(QuartzPluginContext.class);
	}

}
