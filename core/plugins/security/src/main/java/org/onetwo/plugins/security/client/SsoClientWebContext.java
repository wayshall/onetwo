package org.onetwo.plugins.security.client;

import javax.annotation.Resource;

import org.onetwo.plugins.security.client.controller.CrossdomainSessionSsoLoginController;
import org.onetwo.plugins.security.client.controller.CrossdomainSsoLogoutController;
import org.onetwo.plugins.security.client.controller.SsoLoginController;
import org.onetwo.plugins.security.client.controller.SsoLogoutController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SsoClientWebContext {
	
	@Resource
	private SsoClientConfig ssoClientConfig;

	@Bean
	public SsoLoginController ssoLoginController(){
		if(ssoClientConfig.isCrossdomainSession()){
			return new CrossdomainSessionSsoLoginController();
		}
		return new SsoLoginController();
	}
	
	@Bean
	public SsoLogoutController ssoLogoutController(){
		if(ssoClientConfig.isCrossdomainSession()){
			return new CrossdomainSsoLogoutController();
		}
		return new SsoLogoutController();
	}
	
	
}
