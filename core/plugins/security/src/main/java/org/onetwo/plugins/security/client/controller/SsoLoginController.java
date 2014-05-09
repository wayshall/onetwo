package org.onetwo.plugins.security.client.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.onetwo.plugins.security.common.SsoConfig;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/client")
public class SsoLoginController extends PluginSupportedController {

	@Resource
	private SsoConfig ssoConfig;
	
	@RequestMapping("ssologin")
//	@ResponseBody
	public void ssologin(@Valid SsoLoginParams login, BindingResult bind, HttpServletResponse response){
		DataResult dr = null;
		if(bind.hasErrors()){
			dr = DataResult.createFailed("login error");
			ResponseUtils.renderJsonp(response, login.getCallback(), dr);
			return ;
		}
		if(!checkSign(login)){
			logger.error("check sign error");
			dr = DataResult.createFailed("sign error");
			ResponseUtils.renderJsonp(response, login.getCallback(), dr);
			return ;
		}
		ResponseUtils.addP3PHeader(response);
		ResponseUtils.setHttpOnlyCookie(response, UserDetail.TOKEN_KEY, login.getTk());
		logger.info("set cookies succeed!");
		dr = DataResult.createSucceed(BaseSiteConfig.getInstance().getAppCode());
		ResponseUtils.renderJsonp(response, login.getCallback(), dr);
	}
	
	private boolean checkSign(SsoLoginParams login){
		String source = LangUtils.appendNotBlank(LangUtils.decodeUrl(login.getTk()), "|", ssoConfig.getSignKey());
		boolean valid = MDFactory.createSHA().checkEncrypt(source, login.getSign());
		logger.error("check sign, source[{}] : {}", source, login.getSign());
		return valid;
	}
}
