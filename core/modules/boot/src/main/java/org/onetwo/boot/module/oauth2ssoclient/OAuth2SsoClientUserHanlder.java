package org.onetwo.boot.module.oauth2ssoclient;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorType;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * BaseOAuth2Hanlder
 * 微信公众号网页授权文档：
 * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
 * 
 * OAuth2SsoClientUserHanlder
 * OAuth2SsoClientUserInfo
 * @author wayshall
 * <br/>
 */
public class OAuth2SsoClientUserHanlder {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private OAuth2SsoClientUserRepository<OAuth2SsoClientUserInfo> oauth2UserRepository;

	@Autowired
	protected OAuth2SsoClientProperties oauth2SsoClientProperties;
	protected OAuth2RestClient oauth2RestClient;
	
	public OAuth2SsoClientUserInfo getOAuth2SsoClientUserInfo(OAuth2SsoContext context, HttpServletResponse response) {
//		context.setOauthSsoClientConfig(getOauth2SsoClientConfig(context));
		
		OAuth2SsoClientUserInfo userInfo = null;
		if (StringUtils.isNotBlank(context.getCode())) {
			// 第二步：登录
			userInfo = loginByCode(context);
		} else if (StringUtils.isNotBlank(context.getState())) {
			//只有state参数，表示拒绝
			throw new ServiceException(OAuth2SsoClientErrors.OAUTH2_REJECTED);
		} else {
			// 第一步：跳转
			this.redirect(context, response);
		}
		
		return userInfo;
	}
	
	/***
	 * 从oauth服务器跳回后，验证state，并通过code获取用户信息
	 * @author weishao zeng
	 * @param context
	 * @return
	 */
	public OAuth2SsoClientUserInfo loginByCode(OAuth2SsoContext context) {
		if (context.getOauth2SsoClientConfig()==null) {
//			context.setOauthSsoClientConfig(getOauth2SsoClientConfig(context));
			throw new BaseException("sso cient config nout found!");
		}
		if(!oauth2UserRepository.checkOauth2State(context)){
			// 默认为 HttpRequestStoreService 实现，基于redis
			throw new ServiceException(OAuth2SsoClientErrors.OAUTH2_STATE_ERROR);
		}
		OAuth2SsoClientUserInfo userInfo = fetchOAuth2UserInfoFromServerWithCode(context);
		return userInfo;
	}
	
	/***
	 * 通过code从oauth服务器获取用户信息
	 * @author weishao zeng
	 * @param context
	 * @return
	 */
	public OAuth2SsoClientUserInfo fetchOAuth2UserInfoFromServerWithCode(OAuth2SsoContext context) {
		return oauth2RestClient.getUserInfo(context.getCode());
	}

	protected boolean handleWithoutCodeRequest(OAuth2SsoContext context, HttpServletResponse response, HandlerMethod handler) {
		ErrorType error = null;
		if (StringUtils.isNotBlank(context.getState())) {
			error = OAuth2SsoClientErrors.OAUTH2_REJECTED;
		}
		//如果是ajax请求，不跳转，返回错误信息
		if(RequestUtils.isAjaxRequest(context.getRequest()) || RequestUtils.isAjaxHandlerMethod(handler)){
			DataResult<?> result = DataResults.error(error==null?OAuth2SsoClientErrors.OAUTH2_NOT_AUTHORIZE:error).build();
			ResponseUtils.renderObjectAsJson(response, result);
			return false;
		}
		
		if (error!=null) {
			throw new ServiceException(error);
		}
		this.redirect(context, response);
		return false;
	}
	
	/****
	 * 
 * 微信公众号网页授权文档：
 * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
	 * @author weishao zeng
	 * @param context
	 * @param response
	 */
	public void redirect(OAuth2SsoContext context, HttpServletResponse response) {
		try {
//			AuthorizeData authorizeData = getWechatAuthorizeData(request);
			String authorizeUrl = getAuthorizeUrl(context);
			boolean isDeubg = context.getOauth2SsoClientConfig()!=null && context.getOauth2SsoClientConfig().isDebug();
			if(isDeubg){
				logger.info("[ssoclient oauth2] redirect to authorizeUrl : {}", authorizeUrl);
			}
			response.sendRedirect(authorizeUrl);
		} catch (IOException e) {
			throw new ServiceException("redirect error: " + e.getMessage(), e);
		}
	}
	
	protected String getAuthorizeUrl(OAuth2SsoContext context){
		Oauth2SsoClientConfig ssoClientConfig = context.getOauth2SsoClientConfig();
		
		String redirectUrl = buildRedirectUrl(context);
		// trim一下，避免隐藏空格
		redirectUrl = StringUtils.trim(redirectUrl);
		
		String state = oauth2UserRepository.generateAndStoreOauth2State(context);
//		AuthorizeData authorize = wechatOauth2Client.createAuthorize(redirectUrl, state);
		OAuth2SsoClientAuthorizeData authorize = createAuthorize(ssoClientConfig, redirectUrl, state);
		String authorizeUrl = authorize.toAuthorizeUrl();
		boolean isDeubg = context.getOauth2SsoClientConfig()!=null && context.getOauth2SsoClientConfig().isDebug();
		if (isDeubg) {
			logger.info("[wechat oauth2] authorizeUrl url: {}", authorizeUrl);
		}
		return authorizeUrl;
	}
	
	protected String buildRedirectUrl(OAuth2SsoContext context){
		boolean isDeubg = context.getOauth2SsoClientConfig()!=null && context.getOauth2SsoClientConfig().isDebug();
		String redirectUrl = context.getRedirectUrl();
		if (StringUtils.isBlank(redirectUrl)) {
			redirectUrl = context.getOauth2SsoClientConfig().getOauth2RedirectUri();
			// 从配置文件里获取的url需要encode一下
//			redirectUrl = LangUtils.encodeUrl(redirectUrl);
		}
		if (isDeubg) {
			logger.info("[ssoclient oauth2] wechat config redirect url: {}", redirectUrl);
		}
		//check redirectUri?
		if(StringUtils.isBlank(redirectUrl)){
//			throw new ApiClientException(WechatClientError.OAUTH2_REDIRECT_URL_BLANK);
			HttpServletRequest request = context.getRequest();
			redirectUrl = RequestUtils.buildFullRequestUrl(request.getScheme(), request.getServerName(), 80, request.getRequestURI(), request.getQueryString());
			if (isDeubg) {
				logger.info("[ssoclient oauth2] use default redirect url: {}", redirectUrl);
			}
		}
		redirectUrl = LangUtils.encodeUrl(redirectUrl);
		return redirectUrl;
	}
	
	protected OAuth2SsoClientAuthorizeData createAuthorize(Oauth2SsoClientConfig ssoClientConfig, String redirectUrl, String state) {
		return createAuthorize(ssoClientConfig.getClientId(), ssoClientConfig.getOauth2Scope(), redirectUrl, state, ssoClientConfig.getUserAuthorizationUri());
	}
	
	static public OAuth2SsoClientAuthorizeData createAuthorize(String appid, String scope, String redirectUrl, String state, String userAuthorizationUri){
		return OAuth2SsoClientAuthorizeData.builder()
							.appid(appid)
							.scope(scope)
							.responseType(Oauth2SsoClients.RESPONSE_TYPE_CODE)
							.state(state)
							.redirectUri(redirectUrl)
							.userAuthorizationUri(userAuthorizationUri)
							.build();
	}

	public OAuth2SsoClientUserRepository<OAuth2SsoClientUserInfo> getOauth2UserRepository() {
		return oauth2UserRepository;
	}

	public void setOauth2UserRepository(OAuth2SsoClientUserRepository<OAuth2SsoClientUserInfo> oauth2UserRepository) {
		this.oauth2UserRepository = oauth2UserRepository;
	}

}
