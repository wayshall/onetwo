package org.onetwo.common.spring.plugin.event;

import java.util.List;

import org.onetwo.common.spring.plugin.ContextPluginManager;
import org.onetwo.common.spring.plugin.ContextPluginMeta;

public class ContextConfigRegisterEvent {
	
	final private ContextPluginManager<? extends ContextPluginMeta> contextPluginManager;
	final private List<Class<?>> configClasses;
	public ContextConfigRegisterEvent(
			ContextPluginManager<? extends ContextPluginMeta> contextPluginManager,
			List<Class<?>> configClasses) {
		super();
		this.contextPluginManager = contextPluginManager;
		this.configClasses = configClasses;
	}
	public ContextPluginManager<? extends ContextPluginMeta> getContextPluginManager() {
		return contextPluginManager;
	}
	
	public ContextConfigRegisterEvent registerConfigClasses(Class<?>...configs){
		for(Class<?> c : configs){
			configClasses.add(c);
		}
		return this;
	}
	public List<Class<?>> getConfigClasses() {
		return configClasses;
	}
	

}
