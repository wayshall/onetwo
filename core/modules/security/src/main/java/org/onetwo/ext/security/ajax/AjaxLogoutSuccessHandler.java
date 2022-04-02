package org.onetwo.ext.security.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * @author weishao zeng
 * <br/>
 */
public class AjaxLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler, InitializingBean {
	@Autowired
	private SecurityConfig securityConfig;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		if(RequestUtils.isAjaxRequest(request)){
	        response.setStatus(HttpServletResponse.SC_OK);
		} else {
			super.handle(request, response, authentication);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.setDefaultTargetUrl(this.securityConfig.getLogoutSuccessUrl());
		this.setAlwaysUseDefaultTargetUrl(this.securityConfig.isAlwaysUseDefaultTargetUrl());
	}
	
	

}

