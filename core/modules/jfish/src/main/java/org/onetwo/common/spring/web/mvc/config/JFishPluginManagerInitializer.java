package org.onetwo.common.spring.web.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;

public class JFishPluginManagerInitializer implements PluginManagerInitializer {

	@Override
	public List<Class<?>> initPluginContext(String appEnvironment) {
		final List<Class<?>> annoClasses = new ArrayList<Class<?>>();
		annoClasses.add(JFishMvcConfig.class);
		
		JFishPluginManagerFactory.getPluginManager().registerPluginMvcContextClasses(annoClasses);
		
		return annoClasses;
	}

}
