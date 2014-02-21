package org.onetwo.plugins.fmtag;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;


public class FmtagPlugin extends AbstractContextPlugin<FmtagPlugin> {

	private static FmtagPlugin instance;
	
	
	public static FmtagPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(FmtagPlugin plugin){
		instance = plugin;
	}



}
