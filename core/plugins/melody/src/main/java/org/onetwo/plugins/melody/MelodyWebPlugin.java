package org.onetwo.plugins.melody;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;



public class MelodyWebPlugin extends AbstractJFishPlugin<MelodyWebPlugin> {

	private static MelodyWebPlugin instance;
	
	
	public static MelodyWebPlugin getInstance() {
		return instance;
	}

	public void setPluginInstance(MelodyWebPlugin plugin){
		instance = plugin;
	}

}
