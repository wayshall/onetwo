package org.onetwo.common.spring.web.mvc.config.event;

import java.util.Map;

import org.onetwo.common.fish.plugin.JFishPluginMeta;
import org.onetwo.common.fish.plugin.JFishPluginUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.NoIndexIt;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class GlobalMvcEventListener implements JFishMvcPluginListener {
	private final static Logger logger = JFishLoggerFactory.logger(GlobalMvcEventListener.class);
	private int stepIndex = 0;


	@Override
	public void listening(WebApplicationStartupEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
	}


	@Override
	public void listening(WebApplicationStopEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
	}


	@Override
	public void listening(PropertyEditorRegisterEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
	}


	@Override
	public void listening(ArgumentResolverEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
	}


	@Override
	public void listening(MvcContextConfigRegisterEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
	}



	@Override
	public void listening(final FreeMarkerConfigurerBuildEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
		event.getJfishPluginManager().getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) throws Exception {
				if(event.isHasBuilt()){
					String rspath = e.getWebResourceMeta().getTemplatePath();
					rspath = StringUtils.appendEndWith(rspath, "/");
					event.getFreemarkerConfigurer().addPluginTemplateLoader(e.getPluginInfo().getName(), rspath);
					
					logger.info("add plugin["+e.getPluginInfo().getName()+"]resource path : " + rspath);
				}
			}
			
		});
		
	}
	

	@Override
	public void listening(final MvcContextInitEvent event) {
		logger.info("{} : {}", stepIndex++, event.getClass().getSimpleName());
		final Map<String, String> urlMap = new ManagedMap<String, String>();

		final JFishMvcApplicationContext applicationContext = event.getApplicationContext();
		event.getJfishPluginManager().getPluginMetas().each(new It<JFishPluginMeta>() {

			@Override
			public boolean doIt(JFishPluginMeta element, int index) {
				if(!JFishPluginUtils.getJFishPlugin(element).registerMvcResources()){
					return true;
				}
				final String locations = element.getWebResourceMeta().getStaticResourcePath();
				//TODO cacheSeconds
				final String rsBeanName = ResourceHttpRequestHandler.class.getSimpleName()+"#jfishPlugin#"+element.getPluginInfo().getName();
				applicationContext.registerAndGetBean(rsBeanName, ResourceHttpRequestHandler.class, "locations", locations);
				//  pluginPath/static/** 
				urlMap.put(element.getPluginInfo().getContextPath()+"/static/**", rsBeanName);
				logger.info("mapped plugin["+element.getPluginInfo().getName()+"] resource: [" + locations + "] to [" + element.getPluginInfo().getContextPath()+"/static/**]");
				
				return true;
			}
			
		});
		
		if(logger.isInfoEnabled()){
			logger.info("registerMvcResourcesOfPlugins: " + urlMap);
		}
		applicationContext.registerAndGetBean(SimpleUrlHandlerMapping.class.getSimpleName()+"#jfishPlugin", SimpleUrlHandlerMapping.class, "urlMap", urlMap, "order", Ordered.LOWEST_PRECEDENCE - 1);
		
	}
	
}
