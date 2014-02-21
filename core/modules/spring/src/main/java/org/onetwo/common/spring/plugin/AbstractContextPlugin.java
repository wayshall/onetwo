package org.onetwo.common.spring.plugin;

import java.util.List;

abstract public class AbstractContextPlugin<T> implements ContextPlugin{

	protected ContextPluginMeta pluginMeta;
	
	@Override
	public void init(ContextPluginMeta pluginMeta) {
		this.pluginMeta = pluginMeta;
		this.setPluginInstance((T)this);
	}
	
	abstract public void setPluginInstance(T plugin);


	public ContextPluginMeta getPluginMeta() {
		return pluginMeta;
	}


	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
	}

	/*@Override
	public <T> T getExtComponent(Class<T> extClasss) {
		return null;
	}*/

}
