package org.onetwo.boot.plugin.ftl;

import java.util.Date;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.common.utils.JodatimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

@FreeMarkerViewTools(value="pluginHelper")
public class PluginHelperViewTools {
	
	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private BootSiteConfig bootSiteConfig;

	public String formatDate(Date date, String pattern){
		return JodatimeUtils.format(date, pattern);
	}
	
	public String getBaseURL(){
		String path = bootSiteConfig.getBaseURL() + pluginManager.getCurrentWebPlugin().map(p->p.getContextPath()).orElse("");
		return path;
	}

}
