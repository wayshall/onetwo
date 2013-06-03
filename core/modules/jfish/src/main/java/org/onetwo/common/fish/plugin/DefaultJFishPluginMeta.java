package org.onetwo.common.fish.plugin;

import org.onetwo.common.utils.ReflectUtils;

public class DefaultJFishPluginMeta implements JFishPluginMeta {

	private final PluginNameParser pluginNameParser;
	private final PluginInfo pluginInfo;
	private final JFishPlugin jfishPlugin;
	private final PluginWebResourceMeta webResourceMeta;
	private final PluginConfig pluginConfig;
	
	public DefaultJFishPluginMeta(PluginInfo pluginInfo, PluginNameParser parser) {
		super();
		this.pluginNameParser = parser;
		this.pluginInfo = pluginInfo;
		this.jfishPlugin = ReflectUtils.newInstance(pluginInfo.getPluginClass());
		this.pluginConfig = new PluginConfig(this);
		this.webResourceMeta = new PluginWebResourceMeta(pluginInfo);
	}

	public Class<?> getRootClass(){
		return getJfishPlugin().getClass();
	}
	
	public boolean isClassOfThisPlugin(Class<?> clazz){
		return clazz.getName().startsWith(getRootClass().getPackage().getName());
	}

	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}

	public JFishPlugin getJfishPlugin() {
		return jfishPlugin;
	}

	public PluginWebResourceMeta getWebResourceMeta() {
		return webResourceMeta;
	}

	public PluginConfig getPluginConfig() {
		return pluginConfig;
	}

	public PluginNameParser getPluginNameParser() {
		return pluginNameParser;
	}
	
}
