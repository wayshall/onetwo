package org.onetwo.common.fish.plugin;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.springframework.web.servlet.ModelAndView;

abstract public class PluginSupportedController extends AbstractBaseController {
	
	@Resource
	private JFishPluginManager jfishPluginManager;
	
	private JFishPluginMeta pluginMeta;
	
	private JFishPlugin jfishPlugin;
	
	public PluginSupportedController() {
		super();
		this.jfishPlugin = null;
	}
	
	public PluginSupportedController(JFishPlugin jfishPlugin) {
		super();
		this.jfishPlugin = jfishPlugin;
	}
	

	public JFishPlugin getJfishPlugin() {
		if(jfishPlugin==null){
			JFishPlugin jp = jfishPluginManager.getJFishPluginMetaOf(getClass()).getJFishPlugin();
			if(jp==null){
				throw new BaseException("can not find the plugin object : " + getClass());
			}
			jfishPlugin = jp;
			return jp;
		}
		return jfishPlugin;
	}

	@PostConstruct
	final public void initController(){
//		this.pluginMeta = jfishPlugin.getPluginMeta();//jfishPluginManager.getJFishPluginMetaOf(getClass());
		this.pluginMeta = getJfishPlugin().getPluginMeta();
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
