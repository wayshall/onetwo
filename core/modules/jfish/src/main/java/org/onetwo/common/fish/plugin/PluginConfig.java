package org.onetwo.common.fish.plugin;


public interface PluginConfig {

	void init(JFishWebMvcPluginMeta jfishPluginMeta);

	public String getTemplatePath(String path);
	
	public String getContextPath();
	
	public String getBasedURL(String path);

	public String getContextBasedPath(String path);

}