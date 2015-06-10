package org.onetwo.common.spring.web.mvc.config.event;

import java.util.List;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;

public class MvcContextConfigRegisterEvent {

	final private JFishWebMvcPluginManager jFishPluginManager;
	final private List<Class<?>> configClasses;
	public MvcContextConfigRegisterEvent(JFishWebMvcPluginManager jFishPluginManager,
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
	
	public JFishWebMvcPluginManager getjFishPluginManager() {
		return jFishPluginManager;
	}
	public List<Class<?>> getConfigClasses() {
		return configClasses;
	}
	
	
}
