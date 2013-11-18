package org.onetwo.common.spring.plugin;

import java.util.List;

abstract public class AbstractContextPlugin<P extends ContextPlugin, T extends ContextPluginMeta> implements ContextPlugin{

	protected T pluginMeta;
	

	@SuppressWarnings("unchecked")
	@Override
	public void init(ContextPluginMeta pluginMeta) {
		this.pluginMeta = (T)pluginMeta;
		this.setPluginInstance((P)this);
	}
	
	abstract public void setPluginInstance(P plugin);


	public T getPluginMeta() {
		return pluginMeta;
	}


	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
	}

}
