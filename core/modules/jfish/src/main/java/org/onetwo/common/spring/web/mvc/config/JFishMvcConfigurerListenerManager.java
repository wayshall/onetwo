package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.event.ArgumentResolverEvent;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.google.common.eventbus.EventBus;


public class JFishMvcConfigurerListenerManager {
	
	private EventBus mvcEventBus = new EventBus("mvcConfigureer");

	private JFishList<JFishMvcConfigurerListener> listeners = new JFishList<JFishMvcConfigurerListener>();

	public void notifyAfterMvcConfig(final JFishMvcApplicationContext applicationContext, final JFishMvcConfig mvcConfig, final List<PropertyEditorRegistrar> peRegisttrarList){
		this.listeners.each(new NoIndexIt<JFishMvcConfigurerListener>() {

			@Override
			protected void doIt(JFishMvcConfigurerListener element) {
				element.onMvcPropertyEditorRegistrars(peRegisttrarList);
				element.onMvcInitContext(applicationContext, mvcConfig);
			}
			
		});
	}
	
	public void notifyListenersOnFreeMarkerConfigurer(final JFishFreeMarkerConfigurer configurer, final boolean hasBuilt){
		this.listeners.each(new NoIndexIt<JFishMvcConfigurerListener>() {

			@Override
			protected void doIt(JFishMvcConfigurerListener element) {
				element.onMvcBuildFreeMarkerConfigurer(configurer, hasBuilt);
			}
			
		});
		
	}
	
	public void notifyOnRegisterArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers){
		this.listeners.each(new NoIndexIt<JFishMvcConfigurerListener>() {

			@Override
			protected void doIt(JFishMvcConfigurerListener element) {
				element.onRegisterArgumentResolvers(argumentResolvers);
			}
			
		});
		
	}
	
	public void addListener(JFishMvcConfigurerListener l){
		listeners.add(l);
	}
	
	
	public void postArgumentResolversRegisteEvent(final ArgumentResolverEvent event){
		mvcEventBus.post(event);
		
	}
	
	public void registerListener(Object listener){
		mvcEventBus.register(listener);
	}

}
