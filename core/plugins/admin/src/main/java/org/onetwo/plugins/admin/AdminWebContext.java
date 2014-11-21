package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.controller.AdminController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminWebContext {

	@Bean
	public AdminController adminController(){
		return new AdminController();
	}
}
