package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.DefaultContextPluginMeta;

public class DefaultJFishPluginMeta extends DefaultContextPluginMeta implements JFishPluginMeta {

	private final PluginNameParser pluginNameParser;
	private PluginWebResourceMeta webResourceMeta;
	private PluginConfig pluginConfig;
//	private final ContextPlugin contextPlugin;
	private JFishPlugin jfishPlugin;
	
	public DefaultJFishPluginMeta(JFishPlugin jfishPlugin, ContextPlugin contextPlugin, JFishPluginInfo pluginInfo, PluginNameParser parser) {
		super(contextPlugin, pluginInfo);
		this.jfishPlugin = jfishPlugin;
		this.pluginNameParser = parser;
		if(jfishPlugin!=null){
			PluginConfig pc = this.getJFishPlugin().getPluginConfig();
			pc.init(this);
			this.pluginConfig = pc;
		}else{
//			System.out.println("no plugin");
		}
		this.webResourceMeta = new PluginWebResourceMeta(pluginInfo);
	}

	public Class<?> getRootClass(){
		return getContextPlugin().getClass();
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

	/*@Override
	public JFishPlugin getContextPlugin() {
		return JFishPluginUtils.getJFishPlugin(super.getContextPlugin());
	}*/

	@Override
	public JFishPlugin getJFishPlugin() {
		return jfishPlugin;
	}
}
