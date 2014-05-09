package org.onetwo.plugins.security.client;

import org.onetwo.plugins.security.client.controller.SsoLoginController;
import org.onetwo.plugins.security.client.controller.SsoLogoutController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SsoClientWebContext {

	@Bean
	public SsoLoginController ssoLoginController(){
		return new SsoLoginController();
	}
	
	@Bean
	public SsoLogoutController ssoLogoutController(){
		return new SsoLogoutController();
	}
	
	
}
