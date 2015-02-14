package org.onetwo.plugins.security.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.vo.SsoLogoutParams;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;

public class CrossdomainSsoLogoutController extends SsoLogoutController {
	
	protected void processCookies(SsoLogoutParams logout, HttpServletRequest request, HttpServletResponse response){
		ResponseUtils.removeHttpOnlyCookie(response, UserDetail.TOKEN_KEY);
		if(ssoConfig.isCrossdomainSession()){
			ResponseUtils.removeHttpOnlyCookie(response, SecurityPluginUtils.getJFishCookiesHttpSessionStrategy().getCookieName());
		}
	}
}
