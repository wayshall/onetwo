package org.onetwo.boot.plugin.core;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import org.onetwo.boot.plugin.ftl.PluginNameParser;

public interface PluginManager {

	public Optional<WebPlugin> findPluginByElementClass(Class<?> elementClass);

	public Collection<WebPlugin> getPlugins();
	public WebPlugin getPlugin(String pluginName);
	public PluginNameParser getPluginNameParser();
//	public String getPluginTemplateBasePath(WebPlugin webPlugin);
	public String getPluginTemplateBasePath(String pluginName);

	public Optional<WebPlugin> getCurrentWebPlugin();
	
	default public String resovleAsCurrentPluginPath(String path, Function<WebPlugin, String> valueProvider){
		return getCurrentWebPlugin().map(plugin->{
			String pluginPath = getPluginNameParser().resolvePath(path, plugin.getPluginMeta().getName(), valueProvider.apply(plugin));
			return pluginPath;
		}).orElse(path);
	}
}