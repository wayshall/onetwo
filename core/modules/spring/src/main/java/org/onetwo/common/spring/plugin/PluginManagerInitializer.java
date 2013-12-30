package org.onetwo.common.spring.plugin;

import java.util.List;

public interface PluginManagerInitializer {

	public List<Class<?>> initPluginContext(String appEnvironment);

}