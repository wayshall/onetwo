package org.onetwo.common.spring.plugin;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

abstract public class AbstractContextPlugin<T> implements ContextPlugin{

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
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
