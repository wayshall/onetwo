package org.onetwo.common.web.init;

import java.util.EnumSet;
import java.util.Optional;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.ajaxanywhere.AAFilter;
import org.onetwo.common.spring.web.filter.SpringMultipartFilterProxy;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import com.google.common.collect.ImmutableMap;

/***
 * @author way
 *
 */
public class CommonWebFilterInitializer {

	public void onServletContextStartup(ServletContext servletContext) throws ServletException {
		//encodingFilter
		registeredEncodingFilter(servletContext, CharacterEncodingFilter.class);
		
		//multipartFilter
		registeredMultipartFilter(servletContext, SpringMultipartFilterProxy.class);

		//systemFilter
		registeredInitFilter(servletContext, BaseInitFilter.class);
		/*Optional.ofNullable(getInitFilterClass()).ifPresent(cls->{
			Dynamic initfr = servletContext.addFilter("systemFilter", cls);
			initfr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
			initfr.setAsyncSupported(true);
			initfr.setInitParameter("filterSuffix", "true");
		});*/

		//hiddenHttpMethodFilter
		registeredHiddenMethodFilter(servletContext, HiddenHttpMethodFilter.class);
		/*fr = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
		fr.setAsyncSupported(true);
//		fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME);*/
//						.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		//ajaxAnywhere
		registeredAjaxAnywhere(servletContext, AAFilter.class);
		/*fr = servletContext.addFilter("ajaxAnywhere", AAFilter.class);
		fr.setAsyncSupported(true);
		fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");*/
		
		
	}
	
	protected void registeredEncodingFilter(ServletContext servletContext, Class<? extends Filter> encodingFilterClass){
		Optional.ofNullable(encodingFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("encodingFilter", encodingFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
				frconfig.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
				frconfig.setAsyncSupported(true);
				frconfig.setInitParameters(ImmutableMap.of("encoding", "UTF-8", "forceEncoding", "true"));
			});
		});
	}
	
	protected void registeredMultipartFilter(ServletContext servletContext, Class<? extends Filter> multipartFilterClass){
		Optional.ofNullable(multipartFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("multipartFilter", multipartFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
				frconfig.setAsyncSupported(true);
				frconfig.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
			});
		});
	}
	
	protected void registeredInitFilter(ServletContext servletContext, Class<? extends Filter> initFilterClass){
		Optional.ofNullable(initFilterClass).ifPresent(cls->{
			Dynamic initfr = servletContext.addFilter("systemFilter", cls);
			initfr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
			initfr.setAsyncSupported(true);
			initfr.setInitParameter("filterSuffix", "true");
		});
	}
	
	protected void registeredHiddenMethodFilter(ServletContext servletContext, Class<? extends Filter> hiddenFilterClass){
		Optional.ofNullable(hiddenFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("hiddenHttpMethodFilter", hiddenFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
	//			fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "/*");
//				fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME);
				fr.setAsyncSupported(true);
				fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	//							.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
			});
			
		});
	}

	protected void registeredAjaxAnywhere(ServletContext servletContext, Class<? extends Filter> ajaxFilterClass){
		Optional.ofNullable(ajaxFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("ajaxAnywhere", AAFilter.class);
			fr.setAsyncSupported(true);
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		});
	}


}
