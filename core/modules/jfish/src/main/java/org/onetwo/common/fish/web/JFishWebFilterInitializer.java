package org.onetwo.common.fish.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.ajaxanywhere.AAFilter;
import org.onetwo.common.fish.JFishUtils;
import org.onetwo.common.spring.web.filter.SpringMultipartFilterProxy;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import com.google.common.collect.ImmutableMap;

@Order(JFishUtils.WEBAPP_INITIALIZER_ORDER+5)
public class JFishWebFilterInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		if(!BaseSiteConfig.getInstance().isStartupByInitializer()){
			return ;
		}
		
		//encodingFilter
		Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		fr.setAsyncSupported(true);
		fr.setInitParameters(ImmutableMap.of("encoding", "UTF-8", "forceEncoding", "true"));
		
		//multipartFilter
		fr = servletContext.addFilter("multipartFilter", SpringMultipartFilterProxy.class);
		fr.setAsyncSupported(true);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		//systemFilter
		fr = servletContext.addFilter("systemFilter", BaseInitFilter.class);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		fr.setAsyncSupported(true);
		fr.setInitParameter("filterSuffix", "true");

		//hiddenHttpMethodFilter
		fr = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
		fr.setAsyncSupported(true);
//		fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME);
//						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		//ajaxAnywhere
		fr = servletContext.addFilter("ajaxAnywhere", AAFilter.class);
		fr.setAsyncSupported(true);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		
	}

}
