package org.onetwo.plugins.admin.model;

import org.onetwo.plugins.admin.AdminConfigInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackageClasses=AdminPlugin.class)
public class AdminModelContext {

	@Bean
	public AdminConfigInitializer adminConfigInitializer(){
		return new AdminConfigInitializer();
	}
}
