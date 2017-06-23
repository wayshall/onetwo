package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wayshall
 * <br/>
 */
public class MvcInterceptorAdapter implements MvcInterceptor {

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handler,
			ModelAndView modelAndView) {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handler, Exception ex) {
		
	}

	
	

}
