package org.onetwo.plugins.admin.model;

import org.onetwo.plugins.admin.AdminConfigInitializer;
import org.onetwo.plugins.admin.model.service.impl.AdminAppServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses=AdminAppServiceImpl.class)
public class AdminModelContext {

	@Bean
	public AdminConfigInitializer adminConfigInitializer(){
		return new AdminConfigInitializer();
	}
}
