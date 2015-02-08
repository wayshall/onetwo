package org.onetwo.common.spring.web.mvc.config.event;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishPluginManager;

public class MvcContextConfigRegisterEvent {

	final private JFishPluginManager jFishPluginManager;
	final private List<Class<?>> configClasses;
	public MvcContextConfigRegisterEvent(JFishPluginManager jFishPluginManager,
			List<Class<?>> configClasses) {
		super();
		this.jFishPluginManager = jFishPluginManager;
		this.configClasses = configClasses;
	}

	public MvcContextConfigRegisterEvent registerConfigClasses(Class<?>...configs){
		for(Class<?> c : configs){
			configClasses.add(c);
		}
		return this;
	}
	
	public JFishPluginManager getjFishPluginManager() {
		return jFishPluginManager;
	}
	public List<Class<?>> getConfigClasses() {
		return configClasses;
	}
	
	
}
