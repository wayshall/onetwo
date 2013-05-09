package org.onetwo.common.fish.plugin;

public class PluginWebResourceMeta {
	private PluginInfo pluginInfo;

	public PluginWebResourceMeta(PluginInfo pluginInfo) {
		super();
		this.pluginInfo = pluginInfo;
	}

	public String getWebResourceBasePath(){
		return "classpath:META-INF"+pluginInfo.getContextPath();
	}
	
	public String getTemplatePath(){
		return getWebResourceBasePath() + "/ftl/";
	}
	
	public String getStaticResourcePath(){
		return getWebResourceBasePath() + "/static/";
	}

}
