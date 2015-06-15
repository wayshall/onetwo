package org.onetwo.common.fish.plugin;



public class JFishPluginUtils {
	
//	private final static JFishPlugin EMPTY_INSTANCE = new EmptyJFishPlugin();
	
	public static JFishPlugin getJFishPlugin(JFishWebMvcPluginMeta meta){
//		return meta.getJFishPlugin()==null?JFishPlugin.EMPTY_JFISH_PLUGIN:meta.getJFishPlugin();
		return meta.getJFishPlugin();
	}
	
	/*public static JFishPlugin getJFishPlugin(ContextPlugin plugin){
		if(JFishPlugin.class.isInstance(plugin)){
			return (JFishPlugin) plugin;
		}else{
			return new JFishPluginAdapter(plugin);
		}
	}*/
	
}
