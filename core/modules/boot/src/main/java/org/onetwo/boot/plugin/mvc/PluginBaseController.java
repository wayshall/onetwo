package org.onetwo.boot.plugin.mvc;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
	
	abstract protected WebPlugin getPlugin();
	
	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = getViewName(viewName);
		return BootWebUtils.createModelAndView(moduleMv, models);
	}

	@Override
	protected String getViewName(String viewName){
		String moduleMv = getPlugin().getTemplatePath() + StringUtils.appendStartWithSlash(viewName);
		return moduleMv;
	}
}
