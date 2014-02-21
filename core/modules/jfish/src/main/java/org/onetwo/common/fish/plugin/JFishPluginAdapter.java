package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPluginMeta;

public class JFishPluginAdapter extends AbstractJFishPlugin<JFishPluginAdapter> {
	
	private static JFishPluginAdapter instance = new JFishPluginAdapter();
	
	
	public JFishPluginAdapter() {
		super();
	}

	@Override
	public void setPluginInstance(JFishPluginAdapter plugin) {
		instance = plugin;
	}

	public static JFishPluginAdapter getInstance() {
		return instance;
	}

	public void init(ContextPluginMeta pluginMeta) {
	}

	public void onJFishContextClasses(List<Class<?>> annoClasses) {
	}

	
}
