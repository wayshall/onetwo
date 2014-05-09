package org.onetwo.plugins.admin;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class AdminPlugin extends AbstractContextPlugin<AdminPlugin> {

	private static AdminPlugin instance;
	
	
	public static AdminPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(AdminPlugin plugin){
		instance = plugin;
	}



}
