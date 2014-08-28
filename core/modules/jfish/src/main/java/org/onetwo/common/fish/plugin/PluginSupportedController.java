package org.onetwo.common.fish.plugin;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.springframework.web.servlet.ModelAndView;

abstract public class PluginSupportedController extends AbstractBaseController {
	
	@Resource
	private JFishPluginManager jfishPluginManager;
	
	private JFishPluginMeta pluginMeta;
	
	@PostConstruct
	final public void initController(){
		this.pluginMeta = jfishPluginManager.getJFishPluginMetaOf(getClass());
	}
	
	protected JFishPluginMeta getPluginMeta(){
		return pluginMeta;
	}
	protected String pluginView(String viewName){
		return jfishPluginManager.getPluginNameParser().wrapViewPath(pluginMeta.getPluginInfo().getName(), viewName);
	}
	
	protected ModelAndView pluginMv(String viewName, Object...models){
		return mv(pluginView(viewName), models);
	}
	
	protected ModelAndView pluginRedirectTo(String path, String message){
		return redirectTo(pluginMeta.getPluginInfo().wrapAsContextPath(path), message);
	}

}
