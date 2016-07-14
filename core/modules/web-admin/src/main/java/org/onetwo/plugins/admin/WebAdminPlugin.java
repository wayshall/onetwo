package org.onetwo.plugins.admin;

import org.onetwo.boot.plugin.core.PluginAdapter;
import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.SimplePluginMeta;

public class WebAdminPlugin extends PluginAdapter {
	private final SimplePluginMeta meta = new SimplePluginMeta("web-admin", "0.0.1");

	@Override
	public PluginMeta getPluginMeta() {
		return meta;
	}
	
	

}
