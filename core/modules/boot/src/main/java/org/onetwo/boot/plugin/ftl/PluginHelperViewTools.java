package org.onetwo.boot.plugin.ftl;

import java.util.Date;
import java.util.Map;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.PluginProperties;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.common.utils.JodatimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@FreeMarkerViewTools(value="pluginHelper")
public class PluginHelperViewTools {
	
	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private BootSiteConfig bootSiteConfig;
	@Autowired
	private BootJFishConfig bootJFishConfig;

	public String formatDate(Date date, String pattern){
		return JodatimeUtils.format(date, pattern);
	}
	
	public String getBaseURL(){
		String path = bootSiteConfig.getBaseURL() + pluginManager.getCurrentWebPlugin().map(p->p.getContextPath()).orElse("");
		return path;
	}
	
	public PluginProperties getPluginProperties() {
		Map<String, PluginProperties> plugins = bootJFishConfig.getPlugin();
		
		//先查找是否有相关的映射配置
		String pluginName = pluginManager.getCurrentWebPlugin().get().getPluginMeta().getName();
		PluginProperties pluginProps = plugins.get(pluginName);
		return pluginProps;
	}

}
