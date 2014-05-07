package org.onetwo.plugins.security.client.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
public class SsoLoginController extends PluginSupportedController {

	@RequestMapping("ssologin")
//	@ResponseBody
	public void ssologin(@Valid SsoLoginParams login, BindingResult bind, HttpServletResponse response){
		DataResult dr = null;
		if(bind.hasErrors()){
			dr = DataResult.createFailed("login error!");
			ResponseUtils.renderJsonp(response, login.getCallback(), dr);
			return ;
		}
		ResponseUtils.addP3PHeader(response);
		ResponseUtils.setHttpOnlyCookie(response, UserDetail.TOKEN_KEY, login.getTk());
		dr = DataResult.createSucceed("login succeed");
		ResponseUtils.renderJsonp(response, login.getCallback(), dr);
	}
}
