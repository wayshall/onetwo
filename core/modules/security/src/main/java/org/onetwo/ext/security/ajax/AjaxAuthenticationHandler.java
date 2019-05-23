package org.onetwo.ext.security.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.spring.mvc.utils.DataResults.SimpleResultBuilder;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebUtils;
import org.onetwo.ext.security.jwt.JwtAuthStores;
import org.onetwo.ext.security.jwt.JwtAuthStores.StoreContext;
import org.onetwo.ext.security.jwt.JwtSecurityTokenInfo;
import org.onetwo.ext.security.jwt.JwtSecurityTokenService;
import org.onetwo.ext.security.jwt.JwtSecurityUtils;
import org.onetwo.ext.security.utils.CookieStorer;
import org.onetwo.ext.security.utils.SecurityUtils.SecurityErrors;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;

/****
 * 验证成功或失败后的处理器
 * @author way
 *
 */
public class AjaxAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler, InitializingBean {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	public static final String ERROR_CODE_KEY = AuthenticationException.class.getSimpleName() + ".error_code";
	
//	private String authenticationFailureUrl;
	private AuthenticationFailureHandler failureHandler;
    private AuthenticationSuccessHandler successHandler;
	private JsonMapper mapper = JsonMapper.IGNORE_NULL;
	
	private String authenticationFailureUrl;
//	private boolean alwaysUse = false;
//	private MessageSourceAccessor exceptionMessageAccessor;
	private RequestCache requestCache = new NullRequestCache();
	
	private boolean useJwtToken;
	@Autowired(required=false)
	private JwtSecurityTokenService jwtTokenService;
	private String jwtAuthHeader;
	private JwtAuthStores jwtAuthStores;
	private CookieStorer cookieStorer;

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
	    if(StringUtils.isNotBlank(defaultSuccessUrl)){
	    	this.setDefaultTargetUrl(defaultSuccessUrl);
	    }
	    this.setAlwaysUseDefaultTargetUrl(alwaysUse);
    }

	/*public void setExceptionMessageAccessor(MessageSourceAccessor exceptionMessageAccessor) {
		this.exceptionMessageAccessor = exceptionMessageAccessor;
	}*/
	@Override
	public void afterPropertiesSet() throws Exception {
		if(useJwtToken){
			if(jwtTokenService==null){
				throw new BaseException("not jwtTokenService found!");
			}
			if(StringUtils.isBlank(jwtAuthHeader)){
				jwtAuthHeader = JwtSecurityUtils.DEFAULT_HEADER_KEY;
			}
			Assert.notNull(jwtAuthStores);
		}
		if(authenticationFailureUrl!=null){
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl);
	    }else{
	    	this.failureHandler = new SimpleUrlAuthenticationFailureHandler();
	    }
		
		SimpleUrlAuthenticationSuccessHandler srHandler = null;
		if(isAlwaysUseDefaultTargetUrl()){
			srHandler = new SimpleUrlAuthenticationSuccessHandler();
		}else{
			this.requestCache = new HttpSessionRequestCache();
			SavedRequestAwareAuthenticationSuccessHandler savedHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		    //set HttpSessionRequestCache to save pre request url
			savedHandler.setRequestCache(requestCache);
			srHandler = savedHandler;
		}
	    if(getDefaultTargetUrl()!=null){
	    	srHandler.setDefaultTargetUrl(getDefaultTargetUrl());
	    	srHandler.setAlwaysUseDefaultTargetUrl(isAlwaysUseDefaultTargetUrl());
	    }
        this.successHandler = srHandler;
	}
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		JwtSecurityTokenInfo token = null;
		if(useJwtToken){
			token = this.jwtTokenService.generateToken(authentication);
//			response.addHeader(jwtAuthHeader, token.getToken());
			/*DataResult<?> rs = DataResults.success("登录成功！")
											.data(token)
											.build();
			String text = mapper.toJson(rs);
			ResponseUtils.renderJsonByAgent(request, response, text);*/

			StoreContext ctx = StoreContext.builder()
											.authKey(jwtAuthHeader)
											.request(request)
											.response(response)
											.cookieStorer(cookieStorer)
											.token(token)
											.build();
			jwtAuthStores.saveToken(ctx);
		}
		
		if(RequestUtils.isAjaxRequest(request)){
			String redirectUrl = this.getDefaultTargetUrl();
			String targetUrlParameter = getTargetUrlParameter();
			if (isAlwaysUseDefaultTargetUrl()
					|| (targetUrlParameter != null && StringUtils.isNotBlank(request
							.getParameter(targetUrlParameter)))) {
				redirectUrl = determineTargetUrl(request, response);
			}else{
				SavedRequest saveRequest = this.requestCache.getRequest(request, response);
				if(saveRequest!=null){
					this.requestCache.removeRequest(request, response);
					redirectUrl = saveRequest.getRedirectUrl();
					clearAuthenticationAttributes(request);
				}
			}
			
			Object data = redirectUrl;
			if(token!=null){
				data = ImmutableMap.of("redirectUrl", redirectUrl, "token", token);
			}
			DataResult<?> rs = DataResults.success("登录成功！")
//											.data(authentication.getPrincipal())
											.data(data)
											.build();
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
			/*if(BadCredentialsException.class.isInstance(exception)){
				msg = "用户密码不匹配！";
			}*/
			SimpleResultBuilder<?> builder = DataResults.error("验证失败："+msg);
			
			DataResult<?> rs = buildErrorCode(builder, request, exception)
										.code(SecurityErrors.AUTH_FAILED)
										.build();
			String text = mapper.toJson(rs);
			ResponseUtils.render(response, text, ResponseUtils.JSON_TYPE, true);
		}else{
			this.failureHandler.onAuthenticationFailure(request, response, exception);
		}
    }
	
	private SimpleResultBuilder<?> buildErrorCode(SimpleResultBuilder<?> builder, HttpServletRequest request, AuthenticationException exception){
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
	public void setUseJwtToken(boolean useJwtToken) {
		this.useJwtToken = useJwtToken;
	}
	public void setJwtTokenService(JwtSecurityTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}
	public void setJwtAuthHeader(String jwtAuthHeader) {
		this.jwtAuthHeader = jwtAuthHeader;
	}
	public void setJwtAuthStores(JwtAuthStores jwtAuthStores) {
		this.jwtAuthStores = jwtAuthStores;
	}
	public void setCookieStorer(CookieStorer cookieStorer) {
		this.cookieStorer = cookieStorer;
	}
}
