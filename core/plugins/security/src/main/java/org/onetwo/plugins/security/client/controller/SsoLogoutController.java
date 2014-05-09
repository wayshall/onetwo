package org.onetwo.plugins.security.client.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.vo.SsoLogoutParams;
import org.onetwo.plugins.security.common.SsoConfig;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
public class SsoLogoutController extends PluginSupportedController {

	@Resource
	private SsoConfig ssoConfig;
	
	@RequestMapping("ssologout")
//	@ResponseBody
	public void ssologin(@Valid SsoLogoutParams logout, BindingResult bind, HttpServletResponse response){
		DataResult dr = null;
		if(bind.hasErrors()){
			dr = DataResult.createFailed("logout error");
			ResponseUtils.renderJsonp(response, logout.getCallback(), dr);
			return ;
		}
		ResponseUtils.addP3PHeader(response);
		ResponseUtils.removeHttpOnlyCookie(response, UserDetail.TOKEN_KEY);
		dr = DataResult.createSucceed("logout succeed");
		ResponseUtils.renderJsonp(response, logout.getCallback(), dr);
	}
}
