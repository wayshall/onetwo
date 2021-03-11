package org.onetwo.boot.module.security.oauth2.ssoclient;

import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 
 * @author weishao zeng
 * <br/>
 */
//@Configuration
//@EnableOAuth2Sso
////@ConditionalOnProperty("security.oauth2.sso.loginPath")
//// 定制oauth错误异常
//@Import(OAuth2CustomResultConfiguration.class)
public class OAuth2SsoSecurityConfigurer extends DefaultUrlSecurityConfigurer {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	OAuth2SsoClientProperties properties;
	@Autowired(required=false)
	private AjaxAuthenticationHandler authenticationHandler;
	
	
	public OAuth2SsoSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super(accessDecisionManager);
	}
	

	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		new OAuth2ClientFilterSecurityConfigurer(applicationContext, properties, authenticationHandler).configure(http);
	}

    
    @Bean
	public SsoClientUserInfoRestTemplateCustomizer tobaccoRestTemplateCustomizer(OAuth2SsoClientProperties properties) {
		return new SsoClientUserInfoRestTemplateCustomizer(properties);
	}
	
}
