package org.onetwo.boot.plugin.mvc;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
	private final String PLUGIN_PREFIX = "/plugins";

	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = PLUGIN_PREFIX + StringUtils.appendStartWithSlash(viewName);
		return BootWebUtils.createModelAndView(moduleMv, models);
	}
}
