package org.onetwo.common.fish.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.ajaxanywhere.AAFilter;
import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.web.filter.SpringMultipartFilterProxy;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.util.IntrospectorCleanupListener;

import com.google.common.collect.ImmutableMap;

@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER)
public class JFishWebApplicationInitializer extends AbstractContextLoaderInitializer {
	

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if(app==null){
			registerContextLoaderListener(servletContext);
		}
		servletContext.addListener(IntrospectorCleanupListener.class);
		//encodingFilter
		FilterRegistration fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		fr.setInitParameters(ImmutableMap.of("encoding", "UTF-8", "forceEncoding", "true"));
		
		//multipartFilter
		servletContext.addFilter("multipartFilter", SpringMultipartFilterProxy.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		//systemFilter
		fr = servletContext.addFilter("systemFilter", BaseInitFilter.class);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		fr.setInitParameter("filterSuffix", "true");

		//hiddenHttpMethodFilter
		servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class)
						.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "springServlet");
//						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		//ajaxAnywhere
		servletContext.addFilter("ajaxAnywhere", AAFilter.class)
						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		
		/*ServletRegistration.Dynamic sr = servletContext.addServlet("springServlet", new JFishDispatcher());
		sr.setLoadOnStartup(1);
		sr.addMapping("/*");*/
	}
	
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		WebApplicationContext webapp = new JFishWebApplicationContext();
		SpringApplication.initApplication(webapp);
		return webapp;
	}
	
	

}
