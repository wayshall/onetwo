package org.onetwo.boot.module.oauth2.ssoclient.filter;

import org.onetwo.boot.module.oauth2.ssoclient.OAuth2SsoClientProperties;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * 定制 OAuth2ClientAuthenticationProcessingFilter 
 * OAuth2SsoCustomConfiguration
 * SsoSecurityConfigurer
 * @author weishao zeng
 * <br/>
 */
public class OAuth2ClientFilterSecurityConfigurer {

	private ApplicationContext applicationContext;
	private OAuth2SsoClientProperties properties;
	private AjaxAuthenticationHandler authenticationHandler;

	public OAuth2ClientFilterSecurityConfigurer(ApplicationContext applicationContext, OAuth2SsoClientProperties properties, AjaxAuthenticationHandler authenticationHandler) {
		super();
		this.applicationContext = applicationContext;
		this.properties = properties;
		this.authenticationHandler = authenticationHandler;
	}
	
	
	private OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter() {
		OAuth2RestOperations restTemplate = this.applicationContext
				.getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
		ResourceServerTokenServices tokenServices = this.applicationContext
				.getBean(ResourceServerTokenServices.class);
		OAuth2ClientAuthenticationProcessingFilter filter = new ExtOAuth2ClientAuthenticationProcessingFilter(
				properties.getLoginPath());
		filter.setRestTemplate(restTemplate);
		filter.setTokenServices(tokenServices);
		filter.setAuthenticationFailureHandler(authenticationHandler);
		filter.setApplicationEventPublisher(applicationContext);
		filter.setAuthenticationSuccessHandler(authenticationHandler);
		return filter;
	}
	

	public void configure(HttpSecurity http) throws Exception {
		http.apply(new OAuth2ClientAuthenticationConfigurer(oauth2SsoFilter()));
	}


	

	private static class OAuth2ClientAuthenticationConfigurer
			extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

		private OAuth2ClientAuthenticationProcessingFilter filter;

		OAuth2ClientAuthenticationConfigurer(
				OAuth2ClientAuthenticationProcessingFilter filter) {
			this.filter = filter;
		}

		@Override
		public void configure(HttpSecurity builder) throws Exception {
			OAuth2ClientAuthenticationProcessingFilter ssoFilter = this.filter;
			ssoFilter.setSessionAuthenticationStrategy(
					builder.getSharedObject(SessionAuthenticationStrategy.class));
			builder.addFilterAfter(ssoFilter,
					AbstractPreAuthenticatedProcessingFilter.class);
		}

	}
}
