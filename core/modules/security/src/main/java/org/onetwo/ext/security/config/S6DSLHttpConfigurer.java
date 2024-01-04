package org.onetwo.ext.security.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class S6DSLHttpConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    }

	@Override
	public void init(HttpSecurity builder) throws Exception {
		
	}
	
}
