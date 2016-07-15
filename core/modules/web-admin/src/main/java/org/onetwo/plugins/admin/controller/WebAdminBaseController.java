package org.onetwo.plugins.admin.controller;

import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.boot.plugin.core.Plugin;
import org.onetwo.boot.plugin.mvc.PluginBaseController;
import org.onetwo.plugins.admin.WebAdminPlugin;
import org.springframework.beans.factory.annotation.Autowired;

//@PluginContext(contextPath="/web-admin")
abstract public class WebAdminBaseController extends PluginBaseController implements DateInitBinder {

	@Autowired
	private WebAdminPlugin webAdminPlugin;

	@Override
	protected Plugin getPlugin() {
		return webAdminPlugin;
	}
	
	
}
