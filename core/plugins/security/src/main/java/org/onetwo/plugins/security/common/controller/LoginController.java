package org.onetwo.plugins.security.common.controller;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LoginParams;
import org.onetwo.common.sso.UserLoginService;
import org.onetwo.common.utils.UserDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//@Controller
@RequestMapping("/login")
public class LoginController extends NotSsoAbstractLoginController {
	
	@Resource
	private UserLoginService userLoginService;

	@Override
	protected UserDetail doLogin(LoginParams loginParams, SpringSecurityTarget securityTarget) {
		return userLoginService.login(loginParams);
	}

	protected ModelAndView loginSuccessView(LoginParams loginParams, SpringSecurityTarget securityTarget){
//		String view = 
		return mv("index");
	}

}
