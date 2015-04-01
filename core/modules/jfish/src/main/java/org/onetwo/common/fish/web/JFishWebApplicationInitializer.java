package org.onetwo.common.fish.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.spring.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.IntrospectorCleanupListener;

@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER)
public class JFishWebApplicationInitializer extends AbstractContextLoaderInitializer {
	

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if(app==null){
			registerContextLoaderListener(servletContext);
		}
		servletContext.addListener(IntrospectorCleanupListener.class);
	}
	
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		WebApplicationContext webapp = new JFishWebApplicationContext();
		SpringApplication.initApplication(webapp);
		return webapp;
	}
	
	

}
