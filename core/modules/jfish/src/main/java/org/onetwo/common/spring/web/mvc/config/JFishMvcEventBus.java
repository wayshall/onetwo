package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishPlugin;
import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextConfigRegisterEvent;
import org.onetwo.common.spring.web.mvc.config.event.MvcContextInitEvent;
import org.onetwo.common.spring.web.mvc.config.event.PropertyEditorRegisterEvent;
import org.onetwo.common.utils.list.NoIndexIt;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.google.common.eventbus.EventBus;


public class JFishMvcEventBus {
	
	private EventBus mvcEventBus = new EventBus(this.getClass().getSimpleName());
	final private JFishPluginManager jfishPluginManager;
//	private final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	

	public JFishMvcEventBus(JFishPluginManager jfishPluginManager) {
		this.jfishPluginManager = jfishPluginManager;
		registerListener(new GlobalMvcEventListener());
	}

	public void postAfterMvcConfig(final JFishMvcApplicationContext applicationContext, JFishPluginManager jfishPluginManager, final JFishMvcConfig mvcConfig, final List<PropertyEditorRegistrar> peRegisttrarList){
		this.mvcEventBus.post(new PropertyEditorRegisterEvent(peRegisttrarList));
		this.mvcEventBus.post(new MvcContextInitEvent(applicationContext, jfishPluginManager, mvcConfig));
	}
	
	public void postFreeMarkerConfigurer(JFishPluginManager jfishPluginManager, final JFishFreeMarkerConfigurer configurer, final boolean hasBuilt){
		this.mvcEventBus.post(new FreeMarkerConfigurerBuildEvent(jfishPluginManager, configurer, hasBuilt));
	}
	
	public void postArgumentResolversRegisteEvent(List<HandlerMethodArgumentResolver> argumentResolvers){
		mvcEventBus.post(new ArgumentResolverEvent(argumentResolvers));
		
	}
	
	public void postMvcContextConfigRegisterEvent(List<Class<?>> configClasses){
		mvcEventBus.post(new MvcContextConfigRegisterEvent(jfishPluginManager, configClasses));
	}
	
	final public void registerListener(JFishMvcConfigurerListener listener){
		mvcEventBus.register(listener);
	}
	
	final public void registerListenerByPluginManager(JFishPluginManager jfishPluginManager){
		jfishPluginManager.getJFishPlugins().each(new NoIndexIt<JFishPlugin>() {

			@Override
			protected void doIt(JFishPlugin element) throws Exception {
				registerListener(element.getJFishMvcConfigurerListener());
			}
			
		});
	}
	
}
