package org.onetwo.boot.plugins.security;

import org.onetwo.boot.plugins.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class DefaultUrlSecurityConfigurer extends DefaultMethodSecurityConfigurer {
	
	protected void configure(HttpSecurity http) throws Exception {
		webConfigure(http);
		defaultConfigure(http);
	}

	protected void webConfigure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(LOGIN_PATH, LOGIN_PROCESS_PATH, LOGOUT_PATH)
			.permitAll()
			.anyRequest()
			.fullyAuthenticated();
	}
}
