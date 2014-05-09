package org.onetwo.plugins.fmtagext;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class FmtagextPlugin extends AbstractContextPlugin<FmtagextPlugin> {

	public static final String PLUGIN_NAME = "fmtagext";
	public static final String PLUGIN_PATH = "[fmtagext]";

	private static FmtagextPlugin instance;
	
	
	public static FmtagextPlugin getInstance() {
		return instance;
	}
	
	public void setPluginInstance(FmtagextPlugin plugin){
		instance = (FmtagextPlugin)plugin;
	}

}
