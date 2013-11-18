package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.DefaultContextPluginMeta;
import org.onetwo.common.spring.plugin.PluginInfo;

public class DefaultJFishPluginMeta extends DefaultContextPluginMeta<JFishPlugin> implements JFishPluginMeta {

	private final PluginNameParser pluginNameParser;
	private final PluginWebResourceMeta webResourceMeta;
	private final PluginConfig pluginConfig;
	
	public DefaultJFishPluginMeta(PluginInfo pluginInfo, PluginNameParser parser) {
		super(pluginInfo);
		this.pluginNameParser = parser;
		PluginConfig pc = this.getJfishPlugin().getPluginConfig();
		pc.init(this);
		this.pluginConfig = pc;
		this.webResourceMeta = new PluginWebResourceMeta(pluginInfo);
	}

	public Class<?> getRootClass(){
		return getJfishPlugin().getClass();
	}
	
	public boolean isClassOfThisPlugin(Class<?> clazz){
		return clazz.getName().startsWith(getRootClass().getPackage().getName());
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

	@Override
	public JFishPlugin getJfishPlugin() {
		return JFishPluginUtils.getJFishPlugin(super.getJfishPlugin());
	}
}
