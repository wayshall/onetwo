package org.example.web.controller;

import javax.servlet.http.HttpSession;

import org.example.model.member.service.impl.UserLoginServiceImpl;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController extends AbstractBaseController {
	
	@Autowired
	private UserLoginServiceImpl cmUserSSOServiceImpl;
	
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	public ModelAndView logout(HttpSession session){
		cmUserSSOServiceImpl.logout(this.getCurrentUserLogin(session), true);
		JFishWebUtils.removeUserDetail();
		return redirectTo("login");
	}

}
