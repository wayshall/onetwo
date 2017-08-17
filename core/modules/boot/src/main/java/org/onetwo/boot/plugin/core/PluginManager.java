package org.onetwo.boot.plugin.core;

import java.util.Collection;
import java.util.Optional;

import org.onetwo.boot.plugin.ftl.PluginNameParser;

public interface PluginManager {

	public Optional<WebPlugin> findPluginByElementClass(Class<?> elementClass);

	public Collection<WebPlugin> getPlugins();
	public WebPlugin getPlugin(String pluginName);
	public PluginNameParser getPluginNameParser();
//	public String getPluginTemplateBasePath(WebPlugin webPlugin);
	public String getPluginTemplateBasePath(String pluginName);

	public Optional<WebPlugin> getCurrentWebPlugin();
}