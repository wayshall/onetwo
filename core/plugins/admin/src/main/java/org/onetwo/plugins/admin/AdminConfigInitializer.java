package org.onetwo.plugins.admin;

import javax.servlet.FilterConfig;

import org.onetwo.common.web.filter.WebFilterInitializers;

public class AdminConfigInitializer implements WebFilterInitializers {

	@Override
	public void onInit(FilterConfig config) {
		config.getServletContext().setAttribute("adminPluginConfig", AdminPlugin.getInstance().getConfig());
	}

}
