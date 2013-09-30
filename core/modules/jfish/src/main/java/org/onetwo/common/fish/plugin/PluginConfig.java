package org.onetwo.common.fish.plugin;

public interface PluginConfig {

	void init(JFishPluginMeta jfishPluginMeta);

	public String getTemplatePath(String path);
	
	public String getContextPath();

}