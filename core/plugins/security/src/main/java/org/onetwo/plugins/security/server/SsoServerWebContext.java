package org.onetwo.plugins.security.server;

import org.onetwo.plugins.security.server.controller.CrossdomainController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SsoServerWebContext {

	@Bean
	public CrossdomainController crossdomainController(){
		return new CrossdomainController();
	}
}
