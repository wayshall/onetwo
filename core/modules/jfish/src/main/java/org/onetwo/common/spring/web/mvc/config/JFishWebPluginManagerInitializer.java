package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.plugin.ContextPluginManagerFactory;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;

/***
 * jfish项目的web插件初始化
 * @author weishao
 *
 */
public class JFishWebPluginManagerInitializer implements PluginManagerInitializer {

	@Override
	public void initPluginContext(String appEnvironment, List<Class<?>> contextClasses) {
		JFishPluginManager jfishPluginManager = (JFishPluginManager)ContextPluginManagerFactory.getContextPluginManager();
//		final List<Class<?>> annoClasses = new ArrayList<Class<?>>();
		contextClasses.add(JFishMvcConfig.class);
		
		JFishPluginManagerFactory.initPluginManager(jfishPluginManager);
		jfishPluginManager.registerPluginMvcContextClasses(contextClasses);
		
//		return annoClasses;
	}

}
