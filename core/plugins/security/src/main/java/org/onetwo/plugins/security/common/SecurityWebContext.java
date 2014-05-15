package org.onetwo.plugins.security.common;

import org.onetwo.common.spring.web.authentic.SecurityWebExceptionResolver;
import org.onetwo.common.spring.web.authentic.SpringAuthenticationInvocation;
import org.onetwo.common.spring.web.authentic.SpringSecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityWebContext {
	
	@Bean
	public SpringSecurityInterceptor springSecurityInterceptor(){
		return new SpringSecurityInterceptor();
	}
	
	@Bean
	public SecurityWebExceptionResolver securityWebExceptionResolver(){
		return new SecurityWebExceptionResolver();
	}
	
	@Bean
	public SsoConfigInitializer ssoConfigInitializer(){
		return new SsoConfigInitializer();
	}
	
	
}
