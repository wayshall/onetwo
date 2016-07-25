package org.onetwo.common.web.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigInitializerListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ConfigInitializer initializer = new ConfigInitializer(sce.getServletContext());
		initializer.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	

}
