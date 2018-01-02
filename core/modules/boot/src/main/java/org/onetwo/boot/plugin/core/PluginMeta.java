package org.onetwo.boot.plugin.core;

import org.apache.commons.lang3.StringUtils;


public interface PluginMeta {
	String getName();
	String getVersion();
	

	static PluginMeta by(Class<? extends WebPlugin> webPluginClass){
		return by(webPluginClass, "1.0.0");
	}
	
	static PluginMeta by(Class<? extends WebPlugin> webPluginClass, String version){
		return new SimplePluginMeta(StringUtils.uncapitalize(webPluginClass.getSimpleName()), version);
	}
}
