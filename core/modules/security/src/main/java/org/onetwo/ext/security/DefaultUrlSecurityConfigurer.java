package org.onetwo.ext.security;

import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {
	
	private AccessDecisionManager accessDecisionManager;
	
	public DefaultUrlSecurityConfigurer(AccessDecisionManager accessDecisionManager) {
		super();
		this.accessDecisionManager = accessDecisionManager;
	}

	protected void configure(HttpSecurity http) throws Exception {
		webConfigure(http);
		defaultConfigure(http);
	}

	protected void webConfigure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.accessDecisionManager(accessDecisionManager)
			.antMatchers(getSecurityConfig().getLoginUrl(), 
							getSecurityConfig().getLoginProcessUrl(), 
							getSecurityConfig().getLogoutUrl())
			.permitAll()
			.anyRequest()
			.fullyAuthenticated();
	}
}
