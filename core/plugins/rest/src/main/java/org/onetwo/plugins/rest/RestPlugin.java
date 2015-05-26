package org.onetwo.plugins.rest;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class RestPlugin extends AbstractContextPlugin<RestPlugin> {

	private static RestPlugin instance;
	
	
	public static RestPlugin getInstance() {
		return instance;
	}

	public void setPluginInstance(RestPlugin plugin){
		instance = plugin;
	}

}
