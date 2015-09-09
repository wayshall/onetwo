package org.onetwo.boot.plugins.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.controller.WebResultCreator;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.MapResult;
import org.onetwo.common.web.utils.ResponseUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AjaxAuthenticationHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler, InitializingBean {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private String authenticationFailureUrl;
	private AuthenticationFailureHandler failureHandler;
    private AuthenticationSuccessHandler successHandler;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	private String authenticationFailureUrl;
	private String defaultTargetUrl;
	private boolean alwaysUse = false;

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

	@Override
	public void afterPropertiesSet() throws Exception {
		if(authenticationFailureUrl!=null){
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl);
	    }else{
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler();
	    }
	    if(defaultTargetUrl!=null){
		    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		    successHandler.setDefaultTargetUrl(defaultTargetUrl);
		    successHandler.setAlwaysUseDefaultTargetUrl(alwaysUse);
	        this.successHandler = successHandler;
	    }else{
	    	this.successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	    }
	}
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
		if(BootWebUtils.isAjaxRequest(request)){
			SimpleDataResult<?> rs = WebResultCreator.simpleResult()
											.success("登录成功！")
											.data(authentication.getPrincipal())
											.buildResult();
			String text = mapper.toJson(rs);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else{
			this.successHandler.onAuthenticationSuccess(request, response, authentication);
		}
    }


	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException,
            ServletException {
		logger.error("login error", exception);
		if(BootWebUtils.isAjaxRequest(request)){
			MapResult rs = MapResult.error(exception.getMessage()+": 找不到用户或密码错误！");
			String text = mapper.toJson(rs);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else{
			this.failureHandler.onAuthenticationFailure(request, response, exception);
		}
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
