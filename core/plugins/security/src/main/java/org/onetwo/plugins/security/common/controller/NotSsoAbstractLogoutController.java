package org.onetwo.plugins.security.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.authentic.SpringSecurityTarget;
import org.onetwo.common.sso.LogoutParams;
import org.onetwo.common.web.csrf.CsrfValid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@CsrfValid(false)
abstract public class NotSsoAbstractLogoutController extends AbstractBaseController {
	
	abstract protected void doLogout(LogoutParams logoutParams, SpringSecurityTarget securityTarget);

	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView logout(@ModelAttribute("logout") LogoutParams logout, SpringSecurityTarget securityTarget, HttpServletRequest request, HttpServletResponse response){
		
//		String token = securityTarget.getCookieToken();
//		UserDetail loginUser = securityTarget.getAuthoritable();
		
		doLogout(logout, securityTarget);
		
		securityTarget.removeCurrentLoginUser();
		securityTarget.removeCookieToken();

		return logoutSuccess(logout, securityTarget);
	}

	protected ModelAndView logoutSuccess(LogoutParams logout, SpringSecurityTarget securityTarget){
		return mv("logout-success");
	}
}
