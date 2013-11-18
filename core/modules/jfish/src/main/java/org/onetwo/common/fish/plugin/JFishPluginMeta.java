package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.ContextPluginMeta;
import org.onetwo.common.spring.plugin.PluginInfo;


public interface JFishPluginMeta extends ContextPluginMeta<JFishPlugin> {

	public boolean isClassOfThisPlugin(Class<?> clazz);
	
	public PluginInfo getPluginInfo();
	
	public PluginWebResourceMeta getWebResourceMeta();
	
	public PluginNameParser getPluginNameParser();
	
	public PluginConfig getPluginConfig();

}
