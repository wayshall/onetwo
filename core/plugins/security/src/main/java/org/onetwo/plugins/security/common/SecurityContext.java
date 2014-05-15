package org.onetwo.plugins.security.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses = { SpringAuthenticationInvocation.class })
public class SecurityContext {
	
	@Bean
	public SsoConfigInitializer ssoConfigInitializer(){
		return new SsoConfigInitializer();
	}
	
	
}
