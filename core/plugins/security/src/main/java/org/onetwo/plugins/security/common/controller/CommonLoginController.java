package org.onetwo.plugins.security.common.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.onetwo.common.exception.LoginException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.sso.UserLoginService;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.plugins.security.common.SecurityConfig;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/common/login")
@CsrfValid(false)
public class CommonLoginController extends PluginSupportedController {

	@Resource
	private UserLoginService userLoginService;
	
	@Resource
	private SecurityConfig securityConfig;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("login") LoginParams loginParams, SpringSecurityTarget securityTarget) {
		return loginView(loginParams, securityTarget);
	}
	
	protected ModelAndView loginView(LoginParams loginParams, SpringSecurityTarget securityTarget){
		return mv(securityConfig.getLoginView());
	}
	
	protected UserDetail doLogin(LoginParams loginParams, SpringSecurityTarget securityTarget) {
		return userLoginService.login(loginParams);
	}
	
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
			String msg = StringUtils.firstNotBlank(this.getMessage(e.getCode()), e.getMessage());
			return mv(securityConfig.getLoginView(), MESSAGE, msg);
		} catch (Exception e) {
			logger.error("login error", e);
			return mv(securityConfig.getLoginView(), MESSAGE, e.getMessage());
		}
		
		securityTarget.setCurrentLoginUser(userLogin);
		securityTarget.setCookieToken(userLogin.getToken());
		
		return loginSuccessView(loginParams, securityTarget);
	}
	
	protected ModelAndView loginSuccessView(LoginParams loginParams, SpringSecurityTarget securityTarget){
		return mv(securityConfig.getLoginSuccessView());
	}

}
