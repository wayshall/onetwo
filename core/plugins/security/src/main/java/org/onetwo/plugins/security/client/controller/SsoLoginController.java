package org.onetwo.plugins.security.client.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.plugins.security.client.SsoClientConfig;
import org.onetwo.plugins.security.client.vo.SsoLoginParams;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/client")
public class SsoLoginController extends PluginSupportedController {

	@Resource
	private SsoClientConfig ssoClientConfig;

	@RequestMapping("login")
	public ModelAndView login(){
		return pluginMv("login");
	}
	
	@RequestMapping("ssologin")
//	@ResponseBody
	public void ssologin(SsoLoginParams login, HttpServletRequest request, HttpServletResponse response){
		String refereUrl = RequestUtils.getRefereURL(request);
		logger.error("sso client login : token[{}], refereUrl[{}]", login.getTk(), refereUrl);
		DataResult dr = null;
		if(StringUtils.isBlank(login.getTk())){
			dr = DataResult.createFailed("login error");
			ResponseUtils.renderJsonp(response, login.getCallback(), dr);
			return ;
		}
		String serverUrl = ssoClientConfig.getServerUrl();
		if(!refereUrl.contains(serverUrl)){
			dr = DataResult.createFailed("ssologin invali request");
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
		return SecurityPluginUtils.checkSign(login.getTk(), ssoClientConfig.getSignKey(), login.getSign());
	}
}
