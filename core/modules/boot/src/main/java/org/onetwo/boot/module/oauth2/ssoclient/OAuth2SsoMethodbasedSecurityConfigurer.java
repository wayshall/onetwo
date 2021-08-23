package org.onetwo.boot.module.oauth2.ssoclient;

import org.onetwo.boot.module.oauth2.ssoclient.filter.OAuth2ClientFilterSecurityConfigurer;
import org.onetwo.boot.module.oauth2.ssoclient.rest.SsoClientUserInfoRestTemplateCustomizer;
import org.onetwo.ext.security.ajax.AjaxAuthenticationHandler;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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
@EnableConfigurationProperties(OAuth2SsoClientProperties.class)
public class OAuth2SsoMethodbasedSecurityConfigurer extends DefaultMethodSecurityConfigurer {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private OAuth2SsoClientProperties properties;
	@Autowired(required=false)
	private AjaxAuthenticationHandler authenticationHandler;
	
	public OAuth2SsoMethodbasedSecurityConfigurer() {
	}
	

	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		new OAuth2ClientFilterSecurityConfigurer(applicationContext, properties, authenticationHandler).configure(http);
	}

    
    @Bean
	public SsoClientUserInfoRestTemplateCustomizer ssoClientRestTemplateCustomizer(OAuth2SsoClientProperties properties) {
		return new SsoClientUserInfoRestTemplateCustomizer(properties);
	}
	
}
