package org.onetwo.boot.plugin.mvc;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	abstract protected WebPlugin getPlugin();
	
	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = getViewName(viewName);
		return BootWebUtils.createModelAndView(moduleMv, models);
	}

	@Override
	protected String getViewName(String viewName){
		String pluginName = getPlugin().getPluginMeta().getName();
		String templatePath = bootJFishConfig.getPlugin().getProperty(pluginName+".templatePath");
		if(StringUtils.isBlank(templatePath)){
			templatePath = pluginManager.getPluginTemplateBasePath(pluginName);
		}
		String moduleMv = templatePath + StringUtils.appendStartWithSlash(viewName);
		return moduleMv;
	}
}
