package org.onetwo.boot.plugin.core;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.PluginProperties;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

abstract public class WebPluginAdapter implements WebPlugin {
	
	private static final String PLUGIN_POSTFIX = "Plugin";
	
	private String contextPath;
	
	@Autowired
	private BootJFishConfig bootJFishConfig;
	
	private Set<Class<?>> pluginMemberClassSet = Sets.newHashSet();
	
	/***
	 * 添加特定class作为此插件的成员
	 * 这样，添加插件路径的时候，即使此类型不在插件内部，也会被认为是插件成员类而添加插件路径
	 * @author wayshall
	 * @param classes
	 * @return
	 */
	final protected WebPluginAdapter addPluginMemberClasses(Class<?>... classes){
		if(!LangUtils.isEmpty(classes)){
			pluginMemberClassSet.addAll(Arrays.asList(classes));
		}
		return this;
	}
	public boolean contains(Class<?> clazz){
		boolean hasContain = clazz.getName().startsWith(getRootClass().getPackage().getName());
		if(!hasContain){
			return pluginMemberClassSet.contains(clazz);
		}
		return hasContain;
	}
	
	public String toString(){
		return this.getPluginMeta().toString();
	}

	public String getContextPath() {
		/*String contextPath = this.contextPath;
		if(contextPath==null){
			contextPath = StringUtils.uncapitalize(getPluginMeta().getName());
			if(contextPath.endsWith(PLUGIN_POSTFIX)){
				contextPath = contextPath.substring(0, contextPath.length()-PLUGIN_POSTFIX.length());
			}
			this.contextPath = StringUtils.appendStartWith(contextPath, "/");;
		}
		return contextPath;*/
		String contextPath = this.contextPath;
		if(contextPath==null){
			contextPath = getPluginContextPathFromConfig().orElseGet(()->{
				String defaultCtxPath = StringUtils.uncapitalize(getPluginMeta().getName());
				if(defaultCtxPath.endsWith(PLUGIN_POSTFIX)){
					defaultCtxPath = defaultCtxPath.substring(0, defaultCtxPath.length()-PLUGIN_POSTFIX.length());
				}
				return defaultCtxPath;
			});
			this.contextPath = StringUtils.appendStartWith(contextPath, "/");
		}
		return contextPath;
	}
	
	protected Optional<String> getPluginContextPathFromConfig(){
		Map<String, PluginProperties> plugins = bootJFishConfig.getPlugin();
		PluginProperties pluginProps = plugins.get(getPluginMeta().getName());
		if(pluginProps==null || StringUtils.isBlank(pluginProps.getContextPath())){
			return Optional.empty();
		}
		return Optional.of(pluginProps.getContextPath());
	}
}
