package org.onetwo.boot.plugin.core;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.context.ApplicationContext;


public interface PluginMeta {
	
	String PLUGIN_POSTFIX = "Plugin";
	
	String getName();
	String getVersion();
	

	static PluginMeta by(Class<? extends WebPlugin> webPluginClass){
		return by(webPluginClass, "1.0.0");
	}
	
	/***
	 * 使用短横杠命名方法
	 * @author weishao zeng
	 * @param webPluginClass
	 * @return
	 */
	static PluginMeta useKebabCaseBy(Class<? extends WebPlugin> webPluginClass){
		return by(webPluginClass, "1.0.0", "-");
	}
	
	static PluginMeta by(Class<? extends WebPlugin> webPluginClass, String version){
		return by(webPluginClass, version, null);
	}
	
	static PluginMeta by(Class<? extends WebPlugin> webPluginClass, String version, String sperator){
		String clsName = webPluginClass.getSimpleName();
		String pluginName = null;
		if (StringUtils.isNotBlank(sperator)) {
			pluginName = StringUtils.convertWithSeperator(clsName, sperator);
		} else {
			pluginName = StringUtils.uncapitalize(clsName);
		}
		return by(pluginName, webPluginClass, version, sperator);
	}
	static PluginMeta by(String pluginName, Class<? extends WebPlugin> webPluginClass, String version, String sperator){
		String clsName = webPluginClass.getSimpleName();
		if(clsName.endsWith(PLUGIN_POSTFIX)){
			clsName = StringUtils.substringBefore(clsName, PLUGIN_POSTFIX);
		}
		return new SimplePluginMeta(pluginName, version);
	}

	public static String resolvePluginContextPath(ApplicationContext applicationContext, final String pluginContextPath){
		String path = SpringUtils.resolvePlaceholders(applicationContext, pluginContextPath);
		if(StringUtils.isNotBlank(path)){
			path = StringUtils.appendStartWithSlash(path);
		}
		return path;
	}
}
