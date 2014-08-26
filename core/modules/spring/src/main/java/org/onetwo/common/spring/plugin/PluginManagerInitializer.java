package org.onetwo.common.spring.plugin;

import java.util.List;

public interface PluginManagerInitializer {

	public void initPluginContext(String appEnvironment, List<Class<?>> contextClasses);

}