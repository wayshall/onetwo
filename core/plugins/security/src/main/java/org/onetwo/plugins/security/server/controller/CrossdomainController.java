package org.onetwo.plugins.security.server.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.onetwo.plugins.security.server.vo.CdLoginParams;
import org.onetwo.plugins.security.server.vo.CrossdomainAction;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RequestMapping("/crossdomain")
@Controller
public class CrossdomainController extends AbstractBaseController {

	@Resource
	protected SsoServerConfig ssoServerConfig;
	
	@RequestMapping(value="/crossdomain", method = { RequestMethod.GET })
	public Object crossdomain(CdLoginParams params, UserDetail loginuser, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = "";
		if(CrossdomainAction.login.toString().equals(params.getAction())){
			url = ssoServerConfig.getClientLoginUrl(params.getSite());
			url = processCrossdomainUrl(params, url, request);
			
		}else if(CrossdomainAction.logout.toString().equals(params.getAction())){
			url = ssoServerConfig.getClientLogoutUrl(params.getSite());
			
//			String sessionToken = httpSessionStrategy.getRequestedSessionId(request);
//			url = TagUtils.appendParam(url, SsoLoginParams.PARAMS_SESSION_ID, sessionToken);
			
			url = TagUtils.appendParam(url, "callback", params.getCallback());
			
		}else{
			return DataResult.createFailed("invalid request");
		}
		
		
		return redirectTo(url);
	}
	
	protected String processCrossdomainUrl(CdLoginParams params, final String url, HttpServletRequest request){
		String newurl = url;
		String token = ResponseUtils.getUnescapeCookieValue(request, UserDetail.TOKEN_KEY);
		newurl = TagUtils.appendParam(newurl, SsoLoginParams.PARAMS_TK, token);
		
		newurl = TagUtils.appendParam(newurl, "callback", params.getCallback());
		String sign = sign(token);
		newurl = TagUtils.appendParam(newurl, "sign", sign);
		
		return newurl;
	}
	
	private String sign(String token){
		return SecurityPluginUtils.sign(token, ssoServerConfig.getSignKey());
	}


}
