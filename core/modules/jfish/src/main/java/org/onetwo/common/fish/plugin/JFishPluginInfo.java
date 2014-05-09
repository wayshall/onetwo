package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.utils.propconf.PropConfig;

public class JFishPluginInfo extends PluginInfo {
	public static final String WEB_PLUGIN_CLASS = "webPluginClass";

	private String webPluginClass;

	public void init(PropConfig prop){
		super.init(prop);
		if(prop.containsKey(WEB_PLUGIN_CLASS)){
			webPluginClass = prop.getAndThrowIfEmpty(WEB_PLUGIN_CLASS);
			this.getProperties().remove(WEB_PLUGIN_CLASS);
		}
	}

	public String getWebPluginClass() {
		return webPluginClass;
	}
	
}
