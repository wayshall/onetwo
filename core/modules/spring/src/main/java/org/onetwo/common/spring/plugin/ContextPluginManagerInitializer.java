package org.onetwo.common.spring.plugin;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;

/*****
 * 插件管理器初始化
 * @author weishao
 *
 */
public class ContextPluginManagerInitializer implements PluginManagerInitializer {
	
	private static final Logger logger = MyLoggerFactory.getLogger(ContextPluginManagerInitializer.class);
	
	@Override
	public void initPluginContext(String appEnvironment, List<Class<?>> contextClasses){
		SpringUtils.setProfiles(appEnvironment);
		
		String libraryPath = System.getProperty("java.library.path");
		logger.info("java.library.path: {}", libraryPath);
		libraryPath = System.getProperty("jna.library.path");
		logger.info("jna.library.path: {}", libraryPath);
		
		ContextPluginManager<?> jpm = createPluginManager(appEnvironment);
		jpm.scanPlugins();
		//publish event
		jpm.getEventBus().postRegisterJFishContextClasses(contextClasses);
		
//		final List<Class<?>> contextClasses = LangUtils.newArrayList();
//		contextClasses.add(ClassPathApplicationContext.class);
//		contextClasses.addArray(outerContextClasses);
//		jpm.registerPluginJFishContextClasses(contextClasses);

//		return contextClasses;
	}
	
	protected ContextPluginManager<?> createPluginManager(String appEnvironment){
		ContextPluginManager<?> contextPluginManager = new SpringContextPluginManager<ContextPluginMeta>(appEnvironment);
		ContextPluginManagerFactory.initContextPluginManager(contextPluginManager);
		return contextPluginManager;
	}

}
