package org.onetwo.common.fish.web;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.JFishDispatcher;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER)
public class JFishSpringDispatcherServletInitializer extends AbstractDispatcherServletInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {
		if(JFishUtils.isSpringBoot()){
			return ;
		}
		
		if(!BaseSiteConfig.getInstance().isStartupByInitializer()){
			return ;
		}
		super.onStartup(servletContext);
	}
	
	@Override
	protected WebApplicationContext createServletApplicationContext() {
		return null;
	}

	protected void registerDispatcherServlet(ServletContext servletContext) {
		if(JFishUtils.isSpringBoot()){
			return ;
		}
		servletContext.addListener(IntrospectorCleanupListener.class);
		
		String servletName = getServletName();
		Assert.hasLength(servletName, "getServletName() may not return empty or null");

//		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, JFishDispatcher.class);
		Assert.notNull(registration,
				"Failed to register servlet with name '" + servletName + "'." +
				"Check if there is another servlet registered under the same name.");

		registration.setLoadOnStartup(1);
		registration.addMapping(getServletMappings());
		registration.setAsyncSupported(isAsyncSupported());

		Filter[] filters = getServletFilters();
		if (!ObjectUtils.isEmpty(filters)) {
			for (Filter filter : filters) {
				registerServletFilter(servletContext, filter);
			}
		}

		customizeRegistration(registration);
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		if(JFishUtils.isSpringBoot()){
			return null;
		}
		WebApplicationContext webapp = new JFishWebApplicationContext();
		SpringApplication.initApplication(webapp);
		return webapp;
	}
	
	

}
