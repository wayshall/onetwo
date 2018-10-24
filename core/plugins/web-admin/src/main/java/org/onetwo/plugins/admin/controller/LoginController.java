package org.onetwo.plugins.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.plugin.mvc.annotation.WebPluginContext;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.ext.security.provider.CaptchaAuthenticationProvider;
import org.onetwo.plugins.admin.utils.Codes.ErrorCodes;
import org.onetwo.plugins.admin.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@WebPluginContext(contextPath="")
@RequestMapping(WebConstant.CONFIG_LOGIN_PATH)
public class LoginController extends WebAdminBaseController {
	
	@Autowired(required=false)
	private CaptchaAuthenticationProvider captchaAuthenticationProvider;

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request){
		return responsePageOrData("/login", ()->{
			return DataResults.error("请先登录！").code(ErrorCodes.NOT_LOGIN).build();
		});
	}
	
	@ModelAttribute("isCaptcha")
	public boolean isCaptcha(){
		return captchaAuthenticationProvider!=null;
	}

}
