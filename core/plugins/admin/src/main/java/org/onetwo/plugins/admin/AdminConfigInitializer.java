package org.onetwo.plugins.admin;

import javax.servlet.FilterConfig;

import org.onetwo.common.web.filter.WebFilterInitializers;
import org.onetwo.plugins.admin.utils.AdminPluginConfig;

public class AdminConfigInitializer implements WebFilterInitializers {

	@Override
	public void onInit(FilterConfig config) {
		config.getServletContext().setAttribute("adminPluginConfig", AdminPluginConfig.getInstance());
	}

}
