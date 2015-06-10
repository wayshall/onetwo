package org.onetwo.common.fish.plugin;



public final class JFishPluginManagerFactory {

	private static JFishWebMvcPluginManager jfishPluginManager;
	
	private JFishPluginManagerFactory(){
	}
	

	public static void initPluginManager(JFishWebMvcPluginManager jpm){
		jfishPluginManager = jpm;
	}
	
	public static JFishWebMvcPluginManager getPluginManager(){
		return jfishPluginManager;
	}

}