package org.onetwo.boot.plugin.core;

import java.util.List;
import java.util.Optional;

public interface PluginManager {

	public Optional<WebPlugin> findPluginByElementClass(Class<?> elementClass);

	public List<WebPlugin> getPlugins();

}