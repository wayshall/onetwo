package org.onetwo.plugins.security.common;

import javax.servlet.FilterConfig;

import org.onetwo.common.web.filter.WebFilterInitializers;

public class SecurityConfigInitializer implements WebFilterInitializers {
//	@Resource
	private SecurityConfig securityConfig;

	@Override
	public void onInit(FilterConfig config) {
		config.getServletContext().setAttribute("ssoConfig", securityConfig);//兼容
		config.getServletContext().setAttribute("securityConfig", securityConfig);
	}

	public SecurityConfig getSecurityConfig() {
		return securityConfig;
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}
	

}
