package org.onetwo.plugins.security.server.controller;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.plugins.security.server.SsoServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CsrfValid(false)
abstract public class AbstractLoginController extends AbstractBaseController {

	@Autowired
	private SessionStorer sessionStorer;
	
	@Resource
	private SsoServerConfig ssoServerConfig;

	abstract protected UserDetail doLogin(LoginParams loginParams, HttpServletRequest request, HttpServletResponse response);
	
	@RequestMapping(method = { RequestMethod.POST })
	public ModelAndView login(@Valid @ModelAttribute("login") LoginParams loginParams, BindingResult bind, SpringSecurityTarget securityTarget, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (bind.hasErrors()) {
			return mv("login");
		}
		UserDetail userLogin = null;
		try {
//			Map<String, String> map = LangUtils.asMap(SecurityPluginUtils.LOGIN_PARAM_CLIENT_CODE, loginParams.getClientCode());
			userLogin = doLogin(loginParams, request, response);
		} catch (LoginException e) {
			return mv("login", MESSAGE, e.getMessage());
		} catch (Exception e) {
			logger.error("login error", e);
			return mv("login", MESSAGE, e.getMessage());
		}
		
		Collection<String> ssoSites = ssoServerConfig.getClientNames();
		if(!ssoSites.contains(loginParams.getClientCode())){
			throw new ServiceException("非法请求: " + loginParams.getClientCode());
		}

		if(!loginParams.isAll()){
			ssoSites = LangUtils.asList(loginParams.getClientCode());
		}
		
		loginParams.setReturnUrl(ssoServerConfig.getClientUrl(loginParams.getClientCode()));

//		JFishWebUtils.setUserDetail(userLogin);
		this.sessionStorer.addUser(userLogin.getToken(), userLogin);
//		ResponseUtils.setHttpOnlyCookie(response, UserDetail.TOKEN_KEY, userLogin.getToken());
		securityTarget.setCookieToken(userLogin.getToken());
		
		return mv("login-success", "ssoSites", JsonMapper.IGNORE_EMPTY.toJson(ssoSites));
	}

}
