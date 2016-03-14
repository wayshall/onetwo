package org.onetwo.boot.core.web.controller;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
	private final String PLUGIN_PREFIX = "/plugins";

	/*@Resource
	private WebRender webRender;

	protected void render(String name, Object dataModel){
		this.render(JFishWebUtils.getServletRequestAttributes().getResponse(), name, dataModel);
	}
	protected void render(HttpServletResponse response, String name, Object dataModel){
		this.render(response, name, dataModel);
	}*/

	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = PLUGIN_PREFIX + StringUtils.appendStartWithSlash(viewName);
		return BootWebUtils.createModelAndView(moduleMv, models);
	}
}
