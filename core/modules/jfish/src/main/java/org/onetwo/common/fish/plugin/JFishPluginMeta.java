package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.ContextPluginMeta;


public interface JFishPluginMeta extends ContextPluginMeta {

	public boolean isClassOfThisPlugin(Class<?> clazz);
	
//	public PluginInfo getPluginInfo();
	
	public PluginWebResourceMeta getWebResourceMeta();
	
	public PluginNameParser getPluginNameParser();
	
	public PluginConfig getPluginConfig();
	
	public JFishPlugin getJFishPlugin();

}
