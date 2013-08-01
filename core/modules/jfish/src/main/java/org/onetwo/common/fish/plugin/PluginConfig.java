package org.onetwo.common.fish.plugin;

import org.onetwo.common.web.config.BaseSiteConfig;


public class PluginConfig {

	public static String PLUGIN_KEY = "pluginConfig";
	
	private PluginInfo pluginInfo;
	private BaseSiteConfig siteConfig;
	private final String templateBasePath;

	public PluginConfig(JFishPluginMeta jfishPluginMeta) {
		this.pluginInfo = jfishPluginMeta.getPluginInfo();
		this.siteConfig = BaseSiteConfig.getInstance();
		this.templateBasePath = jfishPluginMeta.getPluginNameParser().getPluginBasePath(pluginInfo.getName());
	}

	public String getBaseURL(){
		return siteConfig.getBaseURL()+pluginInfo.getContextPath();
	}
	
	public String getContextPath(){
		return pluginInfo.getContextPath();
	}
	public String getStaticURL(){
		return getBaseURL() + "/static/";
	}

	public String getJsPath(){
		return getStaticURL() + "js";
	}
	
	public String getCssPath(){
		return getStaticURL()+ "css";
	}
	
	public String getImagePath(){
		return getStaticURL()+"images";
	}

	public String getTemplateBasePath() {
		return templateBasePath;
	}

	public String getTemplatePath(String path) {
		return templateBasePath+path;
	}
}
