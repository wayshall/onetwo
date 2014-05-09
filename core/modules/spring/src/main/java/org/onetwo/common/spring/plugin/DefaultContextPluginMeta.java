package org.onetwo.common.spring.plugin;



public class DefaultContextPluginMeta implements ContextPluginMeta {

	private final PluginInfo pluginInfo;
	private final ContextPlugin contextPlugin;
	
	public DefaultContextPluginMeta(ContextPlugin contextPlugin, PluginInfo pluginInfo) {
		super();
		this.pluginInfo = pluginInfo;
//		this.contextPlugin = ReflectUtils.newInstance(pluginInfo.getPluginClass());
		this.contextPlugin = contextPlugin;
	}

	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}

	@Override
	public ContextPlugin getContextPlugin() {
		return contextPlugin;
	}

	
}
