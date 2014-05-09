package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.controller.AdminController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackageClasses=AdminPlugin.class)
public class AdminContext {

	@Bean
	public AdminController adminController(){
		return new AdminController();
	}
}
