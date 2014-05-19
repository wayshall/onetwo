package org.onetwo.plugins.security.server.controller;

import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LogoutParams;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CsrfValid(false)
abstract public class AbstractLogoutController extends AbstractBaseController {
	
	@Autowired
	private SessionStorer sessionStorer;
	@Resource
	private SsoServerConfig ssoServerConfig;
	

	abstract protected void doLogout(LogoutParams logoutParams, UserDetail loginUser, HttpServletRequest request, HttpServletResponse response);

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView logout(@ModelAttribute("logout") LogoutParams logout, SpringSecurityTarget securityTarget, HttpServletRequest request, HttpServletResponse response){
		Collection<String> ssoSites = ssoServerConfig.getClientNames();
		if(!ssoSites.contains(logout.getClientCode())){
//			throw new ServiceException("非法请求: " + logout.getClientCode());
		}
		logout.setReturnUrl(ssoServerConfig.getClientUrl(logout.getClientCode()));
		
		if(!logout.isAll()){
			ssoSites = LangUtils.asList(logout.getClientCode());
		}
		
//		String token = RequestUtils.getUnescapeCookieValue(request, UserDetail.TOKEN_KEY);
		String token = securityTarget.getCookieToken();
		UserDetail loginUser = sessionStorer.getUser(token);
		
		doLogout(logout, loginUser, request, response);
		
		sessionStorer.removeUser(token);
		ResponseUtils.removeHttpOnlyCookie(response, UserDetail.TOKEN_KEY);

		return mv("logout-success", "ssoSites", JsonMapper.IGNORE_EMPTY.toJson(ssoSites));
	}

}
