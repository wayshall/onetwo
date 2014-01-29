package org.onetwo.common.spring.plugin;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;

public class ContextPluginManagerInitializer implements PluginManagerInitializer {
	
	@Override
	public List<Class<?>> initPluginContext(String appEnvironment){
		SpringUtils.setProfiles(appEnvironment);
		
		ContextPluginManager jpm = createPluginManager(appEnvironment);
		jpm.scanPlugins();
		
		final List<Class<?>> contextClasses = LangUtils.newArrayList();
//		contextClasses.add(ClassPathApplicationContext.class);
//		contextClasses.addArray(outerContextClasses);
		jpm.registerPluginJFishContextClasses(contextClasses);

		return contextClasses;
	}
	
	protected ContextPluginManager createPluginManager(String appEnvironment){
		return new SpringContextPluginManager<DefaultContextPluginMeta<ContextPlugin>>(appEnvironment);
	}
	
}
