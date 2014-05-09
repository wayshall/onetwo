package org.onetwo.plugins.security.server.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.view.jsp.TagUtils;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.onetwo.plugins.security.server.vo.CdLoginParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/crossdomain")
@Controller
public class CrossdomainController extends AbstractBaseController {

	@Resource
	private SsoServerConfig ssoServerConfig;

	@RequestMapping(method = { RequestMethod.GET })
	public Object crossdomain(CdLoginParams params, UserDetail loginuser, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = "";
		String url = "";
		if("login".equals(params.getAction())){
			url = ssoServerConfig.getClientLoginUrl(params.getSite());
			token = ResponseUtils.getUnescapeCookieValue(request, UserDetail.TOKEN_KEY);
			url = TagUtils.appendParam(url, "tk", token);
			url = TagUtils.appendParam(url, "callback", params.getCallback());
			String sign = sign(token);
			url = TagUtils.appendParam(url, "sign", sign);
			logger.error("sign token[{}] : {}: ", token, sign);
			
		}else if("logout".equals(params.getAction())){
			url = ssoServerConfig.getClientLogoutUrl(params.getSite());
			url = TagUtils.appendParam(url, "callback", params.getCallback());
			
		}else{
			return DataResult.createFailed("invalid request");
		}
		
		
		return redirectTo(url);
	}
	
	private String sign(String...params){
		String source = LangUtils.appendNotBlank(params) + ssoServerConfig.getSignKey();
		return MDFactory.MD5.encryptWithSalt(source);
	}


}
