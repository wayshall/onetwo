package org.onetwo.common.spring.plugin;

import org.onetwo.common.utils.ReflectUtils;


public class DefaultContextPluginMeta<T extends ContextPlugin> implements ContextPluginMeta<T> {

	private final PluginInfo pluginInfo;
	private final T jfishPlugin;
	
	public DefaultContextPluginMeta(PluginInfo pluginInfo) {
		super();
		this.pluginInfo = pluginInfo;
		this.jfishPlugin = ReflectUtils.newInstance(pluginInfo.getPluginClass());
	}

	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}

	@Override
	public T getJfishPlugin() {
		return jfishPlugin;
	}

	
}
