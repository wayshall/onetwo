package org.onetwo.plugins.security.common;

import javax.servlet.FilterConfig;

import org.onetwo.common.web.filter.WebFilterInitializers;

public class SsoConfigInitializer implements WebFilterInitializers {
//	@Resource
	private SsoConfig ssoConfig;

	@Override
	public void onInit(FilterConfig config) {
		config.getServletContext().setAttribute("ssoConfig", ssoConfig);
	}

	public SsoConfig getSsoConfig() {
		return ssoConfig;
	}

	public void setSsoConfig(SsoConfig ssoConfig) {
		this.ssoConfig = ssoConfig;
	}
	

}
