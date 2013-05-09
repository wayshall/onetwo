package org.onetwo.common.fish.plugin;

import org.onetwo.common.fish.exception.JFishException;


public final class JFishPluginManagerFactory {

	private static JFishPluginManager PLUGIN_MANAGER;
	
	private JFishPluginManagerFactory(){
	}
	
	public static void initPluginManager(){
		DefaultPluginManager jpm = new DefaultPluginManager();
		jpm.scanPlugins();
		setPluginManager(jpm);
	}

	private static void setPluginManager(JFishPluginManager jpm){
		if(PLUGIN_MANAGER!=null)
			throw new JFishException("the plugin manager has bean init!");
		PLUGIN_MANAGER = jpm;
	}
	
	public static JFishPluginManager getPluginManager(){
		if(PLUGIN_MANAGER==null){
//			throw new JFishException("JFishPluginManager has not inited!");
			initPluginManager();
		}
		return PLUGIN_MANAGER;
	}

}