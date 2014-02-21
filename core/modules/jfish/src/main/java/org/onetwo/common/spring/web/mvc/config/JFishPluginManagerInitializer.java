package org.onetwo.common.spring.web.mvc.config;

import org.onetwo.common.fish.plugin.DefaultPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginManagerFactory;
import org.onetwo.common.spring.plugin.ContextPluginManagerInitializer;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;

/***
 * jfish项目的插件管理项目初始化
 * @author weishao
 *
 */
public class JFishPluginManagerInitializer extends ContextPluginManagerInitializer implements PluginManagerInitializer {

	@Override
	protected ContextPluginManager createPluginManager(String appEnvironment){
		ContextPluginManager contextPluginManager = new DefaultPluginManager(appEnvironment);
		ContextPluginManagerFactory.initContextPluginManager(contextPluginManager);
		return contextPluginManager;
	}

}
