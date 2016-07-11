package org.onetwo.plugins.admin.controller;

import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.boot.plugin.mvc.PluginBaseController;
import org.onetwo.boot.plugin.mvc.annotation.PluginContext;

@PluginContext(contextPath="/web-admin")
abstract public class WebAdminBaseController extends PluginBaseController implements DateInitBinder {

}
