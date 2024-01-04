package org.onetwo.ext.security;

import org.onetwo.ext.security.config.S6DSLHttpConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security6Configuration {
	@Autowired
	private S6DSLHttpConfigurer dslHttpConfigurer;

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> 
        	authz.anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
//            .authenticationManager(new CustomAuthenticationManager())
        ;
        http.apply((SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>)dslHttpConfigurer);
        return http.build();
    }
}
