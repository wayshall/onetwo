package org.onetwo.common.spring.plugin;


public interface ContextPluginMeta {
	
	public ContextPlugin getContextPlugin();
	
	public PluginInfo getPluginInfo();

	public Class<?> getRootClass();

}
