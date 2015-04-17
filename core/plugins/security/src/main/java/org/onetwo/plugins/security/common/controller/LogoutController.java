package org.onetwo.plugins.security.common.controller;

import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LogoutParams;
import org.onetwo.plugins.security.common.controller.NotSsoAbstractLogoutController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//@Controller	
@RequestMapping("/logout")
public class LogoutController extends NotSsoAbstractLogoutController {
	
	@Override
	protected void doLogout(LogoutParams logoutParams, SpringSecurityTarget securityTarget) {
//		userLoginServiceImpl.logout(securityTarget.getAuthoritable(), true);
	}
	
	protected ModelAndView logoutSuccess(LogoutParams logout, SpringSecurityTarget securityTarget){
		return redirectTo("/login");
	}

	
}
