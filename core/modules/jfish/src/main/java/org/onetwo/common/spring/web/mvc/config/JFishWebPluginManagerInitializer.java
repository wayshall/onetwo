package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.spring.plugin.ContextPluginManagerFactory;
import org.onetwo.common.spring.plugin.PluginManagerInitializer;
import org.onetwo.common.web.config.BaseSiteConfig;

/***
 * jfish项目的web插件初始化
 * @author weishao
 *
 */
public class JFishWebPluginManagerInitializer implements PluginManagerInitializer {

//	static private Logger logger = JFishLoggerFactory.getLogger(JFishWebPluginManagerInitializer.class);
	
	@Override
	public void initPluginContext(String appEnvironment, List<Class<?>> contextClasses) {
		BaseSiteConfig siteconfig = BaseSiteConfig.getInstance();
		JFishPluginManager jfishPluginManager = (JFishPluginManager)ContextPluginManagerFactory.getContextPluginManager();
//		final List<Class<?>> annoClasses = new ArrayList<Class<?>>();
		contextClasses.add(JFishMvcConfig.class);
		contextClasses.add(JspViewConfig.class);

		if(siteconfig.isViewFtlSupported()){
			contextClasses.add(FreemarkerViewConfig.class);
		}
		if(siteconfig.isViewExcelSupported()){
			contextClasses.add(ExcelViewConfig.class);
		}
		if(siteconfig.isViewJsonXmlSupported()){
			contextClasses.add(JsonXmlViewConfig.class);
		}
		
		JFishPluginManagerFactory.initPluginManager(jfishPluginManager);
		jfishPluginManager.getMvcEventBus().registerListenerByPluginManager(jfishPluginManager);
		
		jfishPluginManager.getMvcEventBus().postMvcContextConfigRegisterEvent(contextClasses);
//		jfishPluginManager.registerPluginMvcContextClasses(contextClasses);
		
//		return annoClasses;
	}

}
