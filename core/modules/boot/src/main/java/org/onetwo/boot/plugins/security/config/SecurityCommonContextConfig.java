package org.onetwo.boot.plugins.security.config;

import org.onetwo.boot.plugins.security.mvc.args.SecurityArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Configuration
public class SecurityCommonContextConfig {
	
	@Bean
	public HandlerMethodArgumentResolver securityArgumentResolver(){
		return new SecurityArgumentResolver();
	}

}
