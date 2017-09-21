package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wayshall
 * <br/>
 */
public class MvcInterceptorAdapter implements MvcInterceptor {
	
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass()); 

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		return true;
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
