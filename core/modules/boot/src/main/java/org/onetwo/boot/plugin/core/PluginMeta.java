package org.onetwo.boot.plugin.core;

import org.apache.commons.lang3.StringUtils;


public interface PluginMeta {
	String PLUGIN_POSTFIX = "Plugin";
	
	String getName();
	String getVersion();
	

	static PluginMeta by(Class<? extends WebPlugin> webPluginClass){
		return by(webPluginClass, "1.0.0");
	}
	
	static PluginMeta by(Class<? extends WebPlugin> webPluginClass, String version){
		String clsName = webPluginClass.getSimpleName();
		if(clsName.endsWith(PLUGIN_POSTFIX)){
			clsName = StringUtils.substringBefore(clsName, PLUGIN_POSTFIX);
		}
		clsName = StringUtils.uncapitalize(clsName);
		return new SimplePluginMeta(clsName, version);
	}
}
