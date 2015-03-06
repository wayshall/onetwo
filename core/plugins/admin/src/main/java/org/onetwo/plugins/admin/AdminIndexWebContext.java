package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.web.controller.AdminController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminIndexWebContext {

	@Bean
	public AdminController adminController(){
		return new AdminController();
	}
}
