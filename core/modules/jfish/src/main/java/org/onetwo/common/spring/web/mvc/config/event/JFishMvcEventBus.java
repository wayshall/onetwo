package org.onetwo.common.spring.web.mvc.config.event;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishPlugin;
import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.google.common.eventbus.EventBus;


public class JFishMvcEventBus {
	
	private EventBus mvcEventBus = new EventBus(this.getClass().getSimpleName());
	final private JFishWebMvcPluginManager jfishPluginManager;
//	private final Logger logger = JFishLoggerFactory.logger(this.getClass());

	public JFishMvcEventBus(JFishWebMvcPluginManager jfishPluginManager) {
		this.jfishPluginManager = jfishPluginManager;
		registerListener(new GlobalMvcEventListener());
	}

	public void postWebApplicationStartupEvent(WebApplicationContext webApplicationContext){
		mvcEventBus.post(new WebApplicationStartupEvent(jfishPluginManager, webApplicationContext));
	}
	public void postWebApplicationStartupCompletedEvent(){
		mvcEventBus.post(new WebApplicationStartupCompletedEvent(jfishPluginManager));
	}
	public void postWebApplicationStopEvent(WebApplicationContext webApplicationContext){
		mvcEventBus.post(new WebApplicationStopEvent(jfishPluginManager, webApplicationContext));
	}

	public void postAfterMvcConfig(final JFishMvcApplicationContext applicationContext, JFishWebMvcPluginManager jfishPluginManager, final JFishMvcConfig mvcConfig, final List<PropertyEditorRegistrar> peRegisttrarList){
		this.mvcEventBus.post(new PropertyEditorRegisterEvent(peRegisttrarList));
		this.mvcEventBus.post(new MvcContextInitEvent(applicationContext, jfishPluginManager, mvcConfig));
	}
	
	public void postFreeMarkerConfigurer(JFishWebMvcPluginManager jfishPluginManager, final JFishFreeMarkerConfigurer configurer, final boolean hasBuilt){
		this.mvcEventBus.post(new FreeMarkerConfigurerBuildEvent(jfishPluginManager, configurer, hasBuilt));
	}
	
	public void postArgumentResolversRegisteEvent(List<HandlerMethodArgumentResolver> argumentResolvers){
		mvcEventBus.post(new ArgumentResolverEvent(argumentResolvers));
		
	}
	
	public void postMvcContextConfigRegisterEvent(List<Class<?>> configClasses){
		mvcEventBus.post(new MvcContextConfigRegisterEvent(jfishPluginManager, configClasses));
	}
	
	final public void registerListener(JFishMvcPluginListener listener){
		mvcEventBus.register(listener);
	}
	
	final public void registerListenerByPluginManager(JFishWebMvcPluginManager jfishPluginManager){
		JFishList.wrap(jfishPluginManager.getJFishPlugins()).each(new NoIndexIt<JFishPlugin>() {

			@Override
			protected void doIt(JFishPlugin element) throws Exception {
				registerListener(element.getJFishMvcConfigurerListener());
			}
			
		});
	}
	
}
