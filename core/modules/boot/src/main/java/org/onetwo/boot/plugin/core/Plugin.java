package org.onetwo.boot.plugin.core;

public interface Plugin {
	
	public PluginMeta getPluginMeta();
	
	public Class<?> getContextConfig();

}
