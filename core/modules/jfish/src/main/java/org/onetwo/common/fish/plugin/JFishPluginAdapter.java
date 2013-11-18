package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.ContextPluginMeta;

public class JFishPluginAdapter extends AbstractJFishPlugin<JFishPluginAdapter> {
	
	private static JFishPluginAdapter instance;
	
	private final ContextPlugin plugin;
	
	public JFishPluginAdapter(ContextPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public void setPluginInstance(JFishPluginAdapter plugin) {
		instance = plugin;
	}

	public static JFishPluginAdapter getInstance() {
		return instance;
	}

	public void init(ContextPluginMeta pluginMeta) {
		plugin.init(pluginMeta);
	}

	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		plugin.onJFishContextClasses(annoClasses);
	}

	
}
