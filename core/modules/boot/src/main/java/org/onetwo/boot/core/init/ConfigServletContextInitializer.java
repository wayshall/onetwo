package org.onetwo.boot.core.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.web.filter.ConfigInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializer;

public class ConfigServletContextInitializer implements ServletContextInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ConfigInitializer initializer = new ConfigInitializer(servletContext);
		initializer.initialize();
	}
	

}
