package org.onetwo.plugins.permission;

import org.onetwo.plugins.permission.web.MenuController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan(basePackageClasses=MenuController.class)
public class PermissionMvcContext {
	
	@Bean
	public MenuController menuController(){
		return new MenuController();
	}
}
