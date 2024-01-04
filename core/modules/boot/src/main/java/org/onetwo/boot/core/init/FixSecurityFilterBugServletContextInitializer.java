package org.onetwo.boot.core.init;

import java.io.IOException;
import java.util.EnumSet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.boot.web.servlet.ServletContextInitializer;

/***
 * boot 不启动security的autoconfig时，有bug，会自动注册不受控的filter
 * @author way
 *
 */
public class FixSecurityFilterBugServletContextInitializer implements ServletContextInitializer {
	
	public static class EmptyFilter implements Filter {

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {
			chain.doFilter(request, response);
		}

		@Override
		public void destroy() {
		}
		
	}

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		Dynamic fr = servletContext.addFilter("org.springframework.security.web.access.intercept.FilterSecurityInterceptor#0", EmptyFilter.class);
		if(fr!=null){
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/fuckSpringBootFilterSecurityInterceptor");
			fr.setAsyncSupported(true);
		}
		
		fr = servletContext.addFilter("org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#0", EmptyFilter.class);
		if(fr!=null){
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/fuckSpringBootFilterSecurityInterceptor");
			fr.setAsyncSupported(true);
		}
		
		fr = servletContext.addFilter("casFilter", EmptyFilter.class);
		if(fr!=null){
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/fuckSpringBootFilterSecurityInterceptor");
			fr.setAsyncSupported(true);
		}
    }

}
