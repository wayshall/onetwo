package org.onetwo.ext.security;

import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {
	
	protected void configure(HttpSecurity http) throws Exception {
		webConfigure(http);
		defaultConfigure(http);
	}

	protected void webConfigure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(getSecurityConfig().getLoginUrl(), 
							getSecurityConfig().getLoginProcessUrl(), 
							getSecurityConfig().getLogoutUrl())
			.permitAll()
			.anyRequest()
			.fullyAuthenticated();
	}
}
