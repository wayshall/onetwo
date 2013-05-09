package org.onetwo.common.fish.plugin;


public interface JFishPluginMeta {
	
	public JFishPlugin getJfishPlugin();
	
	public boolean isClassOfThisPlugin(Class<?> clazz);
	
	public PluginInfo getPluginInfo();
	
	public PluginWebResourceMeta getWebResourceMeta();
	
	public PluginNameParser getPluginNameParser();
	
	public PluginConfig getPluginConfig();

}
