package org.onetwo.boot.plugin.core;

import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.PluginProperties;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class WebPluginAdapter implements WebPlugin {
	
	private static final String PLUGIN_POSTFIX = "Plugin";
	
	private String contextPath;
	
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	public String toString(){
		return this.getPluginMeta().toString();
	}

	public String getContextPath() {
		/*String contextPath = this.contextPath;
		if(contextPath==null){
			contextPath = StringUtils.uncapitalize(getPluginMeta().getName());
			if(contextPath.endsWith(PLUGIN_POSTFIX)){
				contextPath = contextPath.substring(0, contextPath.length()-PLUGIN_POSTFIX.length());
			}
			this.contextPath = StringUtils.appendStartWith(contextPath, "/");;
		}
		return contextPath;*/
		String contextPath = this.contextPath;
		if(contextPath==null){
			contextPath = getPluginContextPathFromConfig().orElseGet(()->{
				String defaultCtxPath = StringUtils.uncapitalize(getPluginMeta().getName());
				if(defaultCtxPath.endsWith(PLUGIN_POSTFIX)){
					defaultCtxPath = defaultCtxPath.substring(0, defaultCtxPath.length()-PLUGIN_POSTFIX.length());
				}
				return defaultCtxPath;
			});
			this.contextPath = StringUtils.appendStartWith(contextPath, "/");
		}
		return contextPath;
	}
	
	protected Optional<String> getPluginContextPathFromConfig(){
		Map<String, PluginProperties> plugins = bootJFishConfig.getPlugin();
		PluginProperties pluginProps = plugins.get(getPluginMeta().getName());
		if(pluginProps==null || StringUtils.isBlank(pluginProps.getContextPath())){
			return Optional.empty();
		}
		return Optional.of(pluginProps.getContextPath());
	}
}
