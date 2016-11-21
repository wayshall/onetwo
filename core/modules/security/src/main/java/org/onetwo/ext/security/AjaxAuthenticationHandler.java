package org.onetwo.ext.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator.SimpleResultBuilder;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class AjaxAuthenticationHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler, InitializingBean {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	public static final String ERROR_CODE_KEY = AuthenticationException.class.getSimpleName() + ".error_code";
	
//	private String authenticationFailureUrl;
	private AuthenticationFailureHandler failureHandler;
    private AuthenticationSuccessHandler successHandler;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	private String authenticationFailureUrl;
	private String defaultTargetUrl;
	private boolean alwaysUse = false;
//	private MessageSourceAccessor exceptionMessageAccessor;
	private RequestCache requestCache = new HttpSessionRequestCache();

	public AjaxAuthenticationHandler(){
		this(null, null, false);
	}
	public AjaxAuthenticationHandler(String authenticationFailureUrl){
		this(authenticationFailureUrl, null, false);
	}
	public AjaxAuthenticationHandler(String authenticationFailureUrl, String defaultSuccessUrl){
		this(authenticationFailureUrl, defaultSuccessUrl, false);
	}
	public AjaxAuthenticationHandler(String authenticationFailureUrl, String defaultSuccessUrl, boolean alwaysUse) {
	    super();
	    this.authenticationFailureUrl = authenticationFailureUrl;
	    this.defaultTargetUrl = defaultSuccessUrl;
	    this.alwaysUse = alwaysUse;
    }

	/*public void setExceptionMessageAccessor(MessageSourceAccessor exceptionMessageAccessor) {
		this.exceptionMessageAccessor = exceptionMessageAccessor;
	}*/
	@Override
	public void afterPropertiesSet() throws Exception {
		if(authenticationFailureUrl!=null){
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl);
	    }else{
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler();
	    }
		SavedRequestAwareAuthenticationSuccessHandler srHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	    if(defaultTargetUrl!=null){
	    	srHandler.setDefaultTargetUrl(defaultTargetUrl);
	    	srHandler.setAlwaysUseDefaultTargetUrl(alwaysUse);
	    }
	    srHandler.setRequestCache(requestCache);
        this.successHandler = srHandler;
	}
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
		if(RequestUtils.isAjaxRequest(request)){
			SavedRequest saveRequest = this.requestCache.getRequest(request, response);
			String redirectUrl = this.defaultTargetUrl;
			if(saveRequest!=null){
				redirectUrl = saveRequest.getRedirectUrl();
			}
			SimpleDataResult<?> rs = WebResultCreator.creator().success("登录成功！")
//											.data(authentication.getPrincipal())
											.data(redirectUrl)
											.buildResult();
			String text = mapper.toJson(rs);
			ResponseUtils.renderJsonByAgent(request, response, text);
		}else{
			this.successHandler.onAuthenticationSuccess(request, response, authentication);
		}
    }


	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException,
            ServletException {
		logger.error("login error", exception);
		if(RequestUtils.isAjaxRequest(request)){
			String msg = exception.getMessage();
			if(BadCredentialsException.class.isInstance(exception)){
				msg = "用户密码不匹配！";
			}
			SimpleResultBuilder builder = WebResultCreator.creator().error("验证失败："+msg);
			
			SimpleDataResult<?> rs = buildErrorCode(builder, request, exception).buildResult();
			String text = mapper.toJson(rs);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else{
			this.failureHandler.onAuthenticationFailure(request, response, exception);
		}
    }
	
	private SimpleResultBuilder buildErrorCode(SimpleResultBuilder builder, HttpServletRequest request, AuthenticationException exception){
		/*if(ExceptionCodeMark.class.isInstance(exception)){
			String code = ((ExceptionCodeMark)exception).getCode();
			builder.code(code);
		}else{
			Object codeValue = request.getAttribute(ERROR_CODE_KEY);
			if(codeValue!=null)
				builder.code(codeValue.toString());
		}
		return builder;*/
		return WebUtils.buildErrorCode(builder, request, exception);
	}
	
	public void setAuthenticationFailureUrl(String authenticationFailureUrl) {
		this.authenticationFailureUrl = authenticationFailureUrl;
	}
	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}
	public void setAlwaysUse(boolean alwaysUse) {
		this.alwaysUse = alwaysUse;
	}
	
	

}
