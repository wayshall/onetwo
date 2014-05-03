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
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/client")
public class SsoLoginController extends PluginSupportedController {

	@RequestMapping("ssologin")
	@ResponseBody
	public DataResult ssologin(@Valid SsoLoginParams login, BindingResult bind, HttpServletResponse response){
		if(bind.hasErrors()){
			return DataResult.createFailed("login error!");
		}
		ResponseUtils.setHttpOnlyCookie(response, UserDetail.TOKEN_KEY, login.getTk());
		return DataResult.createSucceed("login succeed");
	}
}
