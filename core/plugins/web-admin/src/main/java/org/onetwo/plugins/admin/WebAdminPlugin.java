package org.onetwo.plugins.admin;

import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.SimplePluginMeta;
import org.onetwo.boot.plugin.core.WebPluginAdapter;

public class WebAdminPlugin extends WebPluginAdapter {
	private final SimplePluginMeta meta = new SimplePluginMeta("web-admin", "0.0.1");

	@Override
	public PluginMeta getPluginMeta() {
		return meta;
	}

}
