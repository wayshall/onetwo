package org.onetwo.common.fish.plugin;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.DefaultContextPluginMeta;

public class DefaultJFishPluginMeta extends DefaultContextPluginMeta implements JFishWebMvcPluginMeta {

	private final PluginNameParser pluginNameParser;
	private PluginWebResourceMeta webResourceMeta;
	private PluginConfig pluginConfig;
//	private final ContextPlugin contextPlugin;
	private JFishPlugin jfishPlugin;
	
	public DefaultJFishPluginMeta(JFishPlugin jfishPlugin, ContextPlugin contextPlugin, JFishPluginInfo pluginInfo, PluginNameParser parser) {
		super(contextPlugin, pluginInfo);
		this.pluginNameParser = parser;
//		if(jfishPlugin!=null && jfishPlugin!=JFishPlugin.EMPTY_JFISH_PLUGIN){
		this.jfishPlugin = jfishPlugin;
		PluginConfig pc = jfishPlugin.getPluginConfig();
		pc.init(this);
		this.pluginConfig = pc;
		
		this.webResourceMeta = new PluginWebResourceMeta(pluginInfo);
	}

	public Class<?> getRootClass(){
		Class<?> rootClass = null;
		if(!getContextPlugin().isEmptyPlugin()){
			rootClass = getContextPlugin().getClass();
		}else if(!getJFishPlugin().isEmptyPlugin()){
			rootClass = getJFishPlugin().getClass();
		}else{
			throw new BaseException("root class not found, please config pluginClass or webPluginClass : " + getPluginInfo().getName());
		}
		return rootClass;
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
