package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.controller.LoginController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={WebAdminContextConfig.class})
public class WebAdminContextConfig {
	
	@Bean
	@ConditionalOnMissingBean(name="loginController")
	public LoginController loginController(){
		return new LoginController();
	}

}
