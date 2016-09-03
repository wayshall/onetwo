package org.onetwo.plugins.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.plugin.mvc.annotation.PluginContext;
import org.onetwo.plugins.admin.utils.Codes.ErrorCodes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//@Controller
@PluginContext(contextPath="")
@RequestMapping("login")
public class LoginController extends WebAdminBaseController {

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request){
		return responsePageOrData("login", ()->{
			return result().error("请先登录！").code(ErrorCodes.NOT_LOGIN).buildResult();
		});
	}

}
