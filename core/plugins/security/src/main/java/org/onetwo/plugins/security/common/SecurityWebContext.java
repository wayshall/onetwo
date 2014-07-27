package org.onetwo.plugins.security.common;

import org.onetwo.common.spring.web.authentic.SecurityWebExceptionResolver;
import org.onetwo.common.spring.web.authentic.SpringAuthenticationInvocation;
import org.onetwo.common.spring.web.authentic.SpringSecurityInterceptor;
import org.onetwo.common.spring.web.authentic.SsoSpringSecurityInterceptor;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityWebContext {
	
	@Bean
	public SpringSecurityInterceptor springSecurityInterceptor(){
		if(SecurityPluginUtils.existServerConfig() || SecurityPluginUtils.existClientConfig())
			return new SsoSpringSecurityInterceptor();
		//SimpleNotSSOServiceImpl
		return new SpringSecurityInterceptor();
	}
	
	@Bean
	public SecurityWebExceptionResolver securityWebExceptionResolver(){
		return new SecurityWebExceptionResolver();
	}
	
	/*@Bean
	public SSOService simpleNotSSOServiceImpl(){
	}*/
	
}
