package org.onetwo.boot.plugin.core;

import org.onetwo.common.utils.StringUtils;

abstract public class WebPluginAdapter implements WebPlugin {
	
	private static final String PLUGIN_POSTFIX = "Plugin";
	
	private String contextPath;
	
	public String toString(){
		return this.getPluginMeta().toString();
	}

	public String getContextPath() {
		String contextPath = this.contextPath;
		if(contextPath==null){
			contextPath = StringUtils.uncapitalize(getPluginMeta().getName());
			if(contextPath.endsWith(PLUGIN_POSTFIX)){
				contextPath = contextPath.substring(0, contextPath.length()-PLUGIN_POSTFIX.length());
			}
			this.contextPath = StringUtils.appendStartWith(contextPath, "/");;
		}
		return contextPath;
	}
}
