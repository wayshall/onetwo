package org.example.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.example.model.member.service.impl.UserLoginServiceImpl;
import org.example.model.member.vo.LoginParams;
import org.example.model.member.vo.LoginUserInfo;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/login")
@Controller
public class LoginController extends AbstractBaseController {
	
	@Autowired
	private UserLoginServiceImpl cmUserSSOServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("login") LoginParams loginParams){
		return mv("login");
	}
	
	@RequestMapping(method={RequestMethod.POST})
	public ModelAndView login(@Valid @ModelAttribute("login") LoginParams loginParams, BindingResult bind, HttpServletRequest request){
		if(RequestMethod.GET.toString().equals(request.getMethod()) || bind.hasErrors()){
			return mv("login");
		}
		LoginUserInfo userLogin = null;
		try {
			userLogin = cmUserSSOServiceImpl.login(loginParams.getUserName(), loginParams.getUserPassword(), null);
		} catch (LoginException e) {
			return mv("login", MESSAGE, e.getMessage());
		}catch (Exception e) {
			logger.error("login error", e);
			return mv("login", MESSAGE, e.getMessage());
		}
		userLogin.setUserName(loginParams.getUserName());
		JFishWebUtils.setUserDetail(userLogin);
		return redirectTo("login");
	}
}
