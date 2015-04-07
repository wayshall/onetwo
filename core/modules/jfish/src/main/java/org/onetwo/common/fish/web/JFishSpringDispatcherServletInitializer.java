package org.onetwo.common.fish.web;

import org.onetwo.common.spring.SpringApplication;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class JFishSpringDispatcherServletInitializer  {

	@Override
	protected WebApplicationContext createServletApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		WebApplicationContext webapp = new JFishWebApplicationContext();
		SpringApplication.initApplication(webapp);
		return webapp;
	}
	
	

}
