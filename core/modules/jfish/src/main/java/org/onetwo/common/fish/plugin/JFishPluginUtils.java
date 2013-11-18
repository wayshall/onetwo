package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.plugin.ContextPlugin;

public class JFishPluginUtils {
	
//	private final static JFishPlugin EMPTY_INSTANCE = new EmptyJFishPlugin();
	
	public static JFishPlugin getJFishPlugin(JFishPluginMeta meta){
		ContextPlugin plugin = meta.getJfishPlugin();
		return getJFishPlugin(plugin);
	}
	
	public static JFishPlugin getJFishPlugin(ContextPlugin plugin){
		if(JFishPlugin.class.isInstance(plugin)){
			return (JFishPlugin) plugin;
		}else{
			return new JFishPluginAdapter(plugin);
		}
	}
	
}
