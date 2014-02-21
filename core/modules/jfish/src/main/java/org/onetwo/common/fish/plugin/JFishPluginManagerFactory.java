package org.onetwo.common.fish.plugin;



public final class JFishPluginManagerFactory {

	private static JFishPluginManager jfishPluginManager;
	
	private JFishPluginManagerFactory(){
	}
	

	public static void initPluginManager(JFishPluginManager jpm){
		jfishPluginManager = jpm;
	}
	
	public static JFishPluginManager getPluginManager(){
		return jfishPluginManager;
	}

}