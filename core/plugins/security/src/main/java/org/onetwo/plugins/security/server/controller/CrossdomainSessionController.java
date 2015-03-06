package org.onetwo.plugins.security.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.onetwo.plugins.security.server.vo.CdLoginParams;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.stereotype.Controller;

@Controller
public class CrossdomainSessionController extends CrossdomainController {
	
	@Override
	protected String processCrossdomainUrl(CdLoginParams params, String url, HttpServletRequest request){
		String eurl = super.processCrossdomainUrl(params, url, request);
		if(ssoServerConfig.isCrossdomainSession()){
			HttpSessionStrategy httpSessionStrategy = SecurityPluginUtils.getHttpSessionStrategy();
			String sessionToken = httpSessionStrategy.getRequestedSessionId(request);
			eurl = TagUtils.appendParam(eurl, SsoLoginParams.PARAMS_SESSION_ID, sessionToken);
		}
		return eurl;
	}
	

}
