package org.onetwo.boot.plugin.mvc;

import java.util.Map;
import java.util.Optional;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.PluginProperties;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


abstract public class PluginBaseController extends AbstractBaseController {
	
//	private static final String VIEW_MAPPING = "viewMapping";
	
	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	abstract protected WebPlugin getPlugin();
	
	protected ModelAndView pluginMv(String viewName, Object... models){
		String moduleMv = getViewName(viewName);
		/*Optional<PluginProperties> pluginProps = getPluginProperties();
		if (pluginProps.isPresent()) {
			models = ArrayUtils.addAll(models, PluginProperties.LAYOUT_KEY, pluginProps.get().getTemplatePath());
		} else {
			models = ArrayUtils.addAll(models, PluginProperties.LAYOUT_KEY, PluginProperties.DEFAULT_LAYOUT);
		}*/
		return BootWebUtils.createModelAndView(moduleMv, models);
	}
	
	/*private String getViewMapping(String viewName){
		String pluginName = getPlugin().getPluginMeta().getName();
		String key = pluginName+"."+VIEW_MAPPING+"."+viewName;
		return key;
	}*/

	/****
	 * jfish: 
	 * 		plugin: 
	 * 			web-admin:
	 * 				templatePath: /admin
	 * 				viewMapping:
                		/login: ~/login #~表示根目录
	 */

	@Override
	protected String getViewName(String viewName){
		PluginProperties pluginProps = getPluginProperties().orElse(null);
//		String key = getViewMapping(viewName);
		if(pluginProps!=null && pluginProps.getViewMapping().containsKey(viewName)){
			String view = pluginProps.getViewMapping().getProperty(viewName);
			return view;
		}

		String pluginName = getPlugin().getPluginMeta().getName();
		String templatePath = pluginProps==null?"":pluginProps.getTemplatePath();
		if(StringUtils.isBlank(templatePath)){
			templatePath = pluginManager.getPluginTemplateBasePath(pluginName);
		}
		String moduleMv = templatePath + StringUtils.appendStartWithSlash(viewName);
		return moduleMv;
	}
	
	protected Optional<PluginProperties> getPluginProperties() {
		Map<String, PluginProperties> plugins = bootJFishConfig.getPlugin();
		
		//先查找是否有相关的映射配置
		String pluginName = getPlugin().getPluginMeta().getName();
		PluginProperties pluginProps = plugins.get(pluginName);
		return Optional.ofNullable(pluginProps);
	}
	
	/*@Override
	protected String getViewName(String viewName){
		JFishProperties plugin = bootJFishConfig.getPlugin();
		
		//先查找是否有相关的映射配置
		String pluginName = getPlugin().getPluginMeta().getName();
		String key = getViewMapping(viewName);
		if(plugin.containsKey(key)){
			String view = plugin.getProperty(key);
			return view;
		}
		
		String templatePath = bootJFishConfig.getPlugin().getProperty(pluginName+".templatePath");
		if(StringUtils.isBlank(templatePath)){
			templatePath = pluginManager.getPluginTemplateBasePath(pluginName);
		}
		String moduleMv = templatePath + StringUtils.appendStartWithSlash(viewName);
		return moduleMv;
	}*/
}
