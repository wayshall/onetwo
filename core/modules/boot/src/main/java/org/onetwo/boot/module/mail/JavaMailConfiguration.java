package org.onetwo.boot.module.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaMailConfiguration {
	
	@Bean
	public JavaMailService javaMailService() {
		return new JavaMailService();
	}

}
