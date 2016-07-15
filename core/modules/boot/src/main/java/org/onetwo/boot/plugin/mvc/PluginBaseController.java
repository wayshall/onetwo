package org.onetwo.boot.plugin.mvc;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.plugin.core.Plugin;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
	
	abstract protected Plugin getPlugin();
	
	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = getPlugin().getTemplatePath() + StringUtils.appendStartWithSlash(viewName);
		return BootWebUtils.createModelAndView(moduleMv, models);
	}
}
