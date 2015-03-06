package org.onetwo.plugins.security.server;

import javax.annotation.Resource;

import org.onetwo.plugins.security.server.controller.CrossdomainController;
import org.onetwo.plugins.security.server.controller.CrossdomainSessionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SsoServerWebContext {
	
	@Resource
	private SsoServerConfig ssoServerConfig;

	@Bean
	public CrossdomainController crossdomainController(){
		if(ssoServerConfig.isCrossdomainSession()){
			return new CrossdomainSessionController();
		}else{
			return new CrossdomainController();
		}
	}
}
