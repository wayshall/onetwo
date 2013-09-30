package org.onetwo.common.fish.plugin;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.springframework.web.servlet.ModelAndView;

abstract public class PluginBaseController extends AbstractBaseController {
	
	@Resource
	private JFishPluginManager jfishPluginManager;
	
	protected String pluginView(String viewName){
		JFishPluginMeta meta = jfishPluginManager.getJFishPluginMetaOf(getClass());
		return jfishPluginManager.getPluginNameParser().wrapViewPath(meta.getPluginInfo().getName(), viewName);
	}
	
	protected ModelAndView pluginMv(String viewName, Object...models){
		return mv(pluginView(viewName), models);
	}

}
