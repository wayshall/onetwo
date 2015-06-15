package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;


public class DefaultPluginConfig implements PluginConfig {

	public static String PLUGIN_KEY = "pluginConfig";
	
	private PluginInfo pluginInfo;
	private BaseSiteConfig siteConfig;
	private String templateBasePath;
	
	public DefaultPluginConfig(){
	}

	@Override
	public void init(JFishWebMvcPluginMeta jfishPluginMeta) {
		this.pluginInfo = jfishPluginMeta.getPluginInfo();
		this.siteConfig = BaseSiteConfig.getInstance();
		this.templateBasePath = jfishPluginMeta.getPluginNameParser().getPluginBasePath(pluginInfo.getName());
	}

	public String getBaseURL(){
		return siteConfig.getBaseURL()+pluginInfo.getContextPath();
	}

	public String getBasedURL(String path){
		return getBaseURL()+StringUtils.appendStartWith(path, "/");
	}

	public String getContextBasedPath(String path){
		return getContextPath() + StringUtils.appendStartWith(path, "/");
	}
	
	public String getContextPath(){
		return pluginInfo.getContextPath();
	}
	public String getStaticURL(){
		return getBaseURL() + "/static";
	}

	public String getJsPath(){
		return getStaticURL() + "/js";
	}
	
	public String getCssPath(){
		return getStaticURL()+ "/css";
	}
	
	public String getImagePath(){
		return getStaticURL()+"/images";
	}

	public String getTemplateBasePath() {
		return templateBasePath;
	}

	public String getTemplatePath(String path) {
		return templateBasePath+path;
	}
}
