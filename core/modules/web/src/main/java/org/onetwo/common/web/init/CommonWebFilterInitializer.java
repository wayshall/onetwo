package org.onetwo.common.web.init;

import java.util.EnumSet;
import java.util.Optional;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.slf4j.Logger;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;

import com.google.common.collect.ImmutableMap;

/***
 * @author way
 *
 */
public class CommonWebFilterInitializer {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	/****
	 * 
	 * isMatchAfter true if the given filter mapping should be matched
     * after any declared filter mappings, and false if it is supposed to
     * be matched before any declared filter mappings of the ServletContext
	 */
	private boolean isMatchAfter = false;

	public void onServletContextStartup(ServletContext servletContext) throws ServletException {
		//encodingFilter
		registeredEncodingFilter(servletContext, CharacterEncodingFilter.class);
		
		//multipartFilter
//		registeredMultipartFilter(servletContext, SpringMultipartFilterProxy.class);
		
		//hiddenHttpMethodFilter 
		registeredHiddenMethodFilter(servletContext, HiddenHttpMethodFilter.class);
		
		//multipartFilter
//		registeredMultipartFilter(servletContext, SpringMultipartFilterProxy.class);

		//systemFilter
		registeredInitFilter(servletContext, BaseInitFilter.class);

		//ajaxAnywhere 
//		registeredAjaxAnywhere(servletContext, AAFilter.class);
		
	}
	
	protected void registeredEncodingFilter(ServletContext servletContext, Class<? extends Filter> encodingFilterClass){
		Optional.ofNullable(encodingFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("characterEncodingFilter", encodingFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
				frconfig.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), isMatchAfter, "/*");
				frconfig.setAsyncSupported(true);
				frconfig.setInitParameters(ImmutableMap.of("encoding", "UTF-8", "forceEncoding", "true"));
				logger.info("FilterInitializer: {} has bean registered!", encodingFilterClass.getSimpleName());
			});
		});
	}
	
	protected void registeredMultipartFilter(ServletContext servletContext, Class<? extends Filter> multipartFilterClass){
		Optional.ofNullable(multipartFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter(MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME, multipartFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
				frconfig.setAsyncSupported(true);
				frconfig.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), isMatchAfter, "/*");
				logger.info("FilterInitializer: {} has bean registered!", multipartFilterClass.getSimpleName());
			});
		});
	}
	
	protected void registeredInitFilter(ServletContext servletContext, Class<? extends Filter> initFilterClass){
		logger.info("registeredInitFilter: {} ", initFilterClass);
		Optional.ofNullable(initFilterClass).ifPresent(cls->{
			logger.info("execute registeredInitFilter ...");
			Dynamic initfr = servletContext.addFilter("systemFilter", cls);
			initfr.addMappingForUrlPatterns(this.getAllDispatcherTypes(), isMatchAfter, "/*");
			initfr.setAsyncSupported(true);
			initfr.setInitParameter("filterSuffix", "true");
			logger.info("registeredInitFilter result: {} has bean registered!", initFilterClass.getSimpleName());
		});
	}
	
	protected void registeredHiddenMethodFilter(ServletContext servletContext, Class<? extends Filter> hiddenFilterClass){
		Optional.ofNullable(hiddenFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter(hiddenFilterClass.getSimpleName(), hiddenFilterClass);
			Optional.ofNullable(fr).ifPresent(frconfig->{
	//			fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "/*");
//				fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME);
				fr.setAsyncSupported(true);
				fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), isMatchAfter, "/*");
	//							.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
				logger.info("FilterInitializer: {} has bean registered!", hiddenFilterClass.getSimpleName());
			});
			
		});
	}

	/*protected void registeredAjaxAnywhere(ServletContext servletContext, Class<? extends Filter> ajaxFilterClass){
		Optional.ofNullable(ajaxFilterClass).ifPresent(cls->{
			Dynamic fr = servletContext.addFilter("ajaxAnywhere", AAFilter.class);
			fr.setAsyncSupported(true);
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), isMatchAfter, "/*");
			logger.info("FilterInitializer: {} has bean registered!", ajaxFilterClass.getSimpleName());
		});
	}*/
	
	protected EnumSet<DispatcherType> getAllDispatcherTypes(){
		return EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
	}


}
