package org.onetwo.plugins.security.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.util.Assert;

public class CrossdomainSessionSsoLoginController extends SsoLoginController {

	protected void processCookies(SsoLoginParams login, HttpServletRequest request, HttpServletResponse response){
		super.processCookies(login, request, response);
		if(ssoClientConfig.isCrossdomainSession()){
			Assert.hasLength(login.getSid(), "sid can not be empty!");
			ResponseUtils.setHttpOnlyCookie(response, SecurityPluginUtils.getJFishCookiesHttpSessionStrategy().getCookieName(), login.getSid());
		}
	}
	
}
