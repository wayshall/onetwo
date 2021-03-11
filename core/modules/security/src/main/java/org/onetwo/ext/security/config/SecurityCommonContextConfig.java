package org.onetwo.ext.security.config;

import org.onetwo.common.spring.condition.OnMissingBean;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.ajax.AjaxLogoutSuccessHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAccessDeniedHandler;
import org.onetwo.ext.security.ajax.AjaxSupportedAuthenticationEntryPoint;
import org.onetwo.ext.security.mvc.args.SecurityArgumentResolver;
import org.onetwo.ext.security.utils.CookieStorer;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecuritySessionUserManager;
import org.onetwo.ext.security.utils.UserPasswordEncoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/****
 * security的通用配置
 * @author way
 *
 */
@Configuration
public class SecurityCommonContextConfig implements InitializingBean{

//	abstract public SecurityConfig getSecurityConfig();
	@Autowired
	private SecurityConfig securityConfig;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(securityConfig, "no securityConfig found!");
	}

	public SecurityConfig getSecurityConfig() {
		return securityConfig;
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}

	@Bean
	public HandlerMethodArgumentResolver securityArgumentResolver(){
		return new SecurityArgumentResolver();
	}
	
	@Bean
	public SessionUserManager<UserDetail> sessionUserManager(){
		return new SecuritySessionUserManager();
	}
	
//	@Bean
//	public BCryptPasswordEncoder bcryptEncoder(){
//		BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
//		return coder;
//	}
	
	@Bean
	public UserPasswordEncoder userPasswordEncoder() {
		return new UserPasswordEncoder();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
		return coder;
	}
	
	@Bean
	@OnMissingBean(AjaxSupportedAccessDeniedHandler.class)
	public AjaxSupportedAccessDeniedHandler ajaxSupportedAccessDeniedHandler(){
		AjaxSupportedAccessDeniedHandler adh = new AjaxSupportedAccessDeniedHandler();
		adh.setErrorPage(securityConfig.getErrorPage());
		return adh;
	}

	@Bean
	@OnMissingBean(AjaxAuthenticationHandler.class)
	public AjaxAuthenticationHandler ajaxAuthenticationHandler(){
		AjaxAuthenticationHandler handler = new AjaxAuthenticationHandler(getSecurityConfig().isFailureUrlWithMessage(), 
																			getSecurityConfig().getFailureUrl(), 
																			getSecurityConfig().getAfterLoginUrl(),
																			getSecurityConfig().isAlwaysUseDefaultTargetUrl());
		if(securityConfig.getJwt().isEnabled()){
			handler.setUseJwtToken(securityConfig.getJwt().isEnabled());
			handler.setJwtAuthHeader(securityConfig.getJwt().getAuthHeader());
			handler.setJwtAuthStores(securityConfig.getJwt().getAuthStore());
			handler.setCookieStorer(cookieStorer());
		}
		return handler;
	}
	
	@Bean
	@OnMissingBean(CookieStorer.class)
	public CookieStorer cookieStorer(){
		CookieStorer cookieStorer = CookieStorer.builder()
												.cookieDomain(this.securityConfig.getCookie().getDomain())
												.cookiePath(this.securityConfig.getCookie().getPath())
												.build();
		return cookieStorer;
	}
	
	@Bean
	@OnMissingBean(AjaxSupportedAuthenticationEntryPoint.class)
	public AjaxSupportedAuthenticationEntryPoint ajaxSupportedAuthenticationEntryPoint(){
		AjaxSupportedAuthenticationEntryPoint ep = new AjaxSupportedAuthenticationEntryPoint();
		ep.setForceHttps(securityConfig.getRedirectStrategy().isForceHttps());
		ep.setHttpsPort(securityConfig.getRedirectStrategy().getHttpsPort());
		ep.setContextRelative(securityConfig.getRedirectStrategy().isContextRelative());
		return ep;
	}
	
	@Bean
	@OnMissingBean(AjaxLogoutSuccessHandler.class)
	public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler(){
		AjaxLogoutSuccessHandler handler = new AjaxLogoutSuccessHandler();
		return handler;
	}
}
