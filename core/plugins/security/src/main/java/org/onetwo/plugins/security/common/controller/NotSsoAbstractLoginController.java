package org.onetwo.plugins.security.common.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.csrf.CsrfValid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CsrfValid(false)
abstract public class NotSsoAbstractLoginController extends AbstractBaseController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("login") LoginParams loginParams, SpringSecurityTarget securityTarget) {
		return loginView(loginParams, securityTarget);
	}
	
	protected ModelAndView loginView(LoginParams loginParams, SpringSecurityTarget securityTarget){
		return mv("login");
	}
	
	abstract protected UserDetail doLogin(LoginParams loginParams, SpringSecurityTarget securityTarget);
	
	@RequestMapping(method = { RequestMethod.POST })
	public ModelAndView login(@Valid @ModelAttribute("login") LoginParams loginParams, BindingResult bind, SpringSecurityTarget securityTarget) throws IOException {
		if (bind.hasErrors()) {
			return loginView(loginParams, securityTarget);
		}
		
		UserDetail userLogin = null;
		try {
//			Map<String, String> map = LangUtils.asMap(SecurityPluginUtils.LOGIN_PARAM_CLIENT_CODE, loginParams.getClientCode());
			userLogin = doLogin(loginParams, securityTarget);
		} catch (LoginException e) {
			return mv("login", MESSAGE, e.getMessage());
		} catch (Exception e) {
			logger.error("login error", e);
			return mv("login", MESSAGE, e.getMessage());
		}
		
		securityTarget.setCurrentLoginUser(userLogin);
		securityTarget.setCookieToken(userLogin.getToken());
		
		return loginSuccessView(loginParams, securityTarget);
	}
	
	protected ModelAndView loginSuccessView(LoginParams loginParams, SpringSecurityTarget securityTarget){
		return mv("login-success");
	}

}
