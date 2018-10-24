package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 和spring 拦截器的接口基本一样，重新定义只是为了区分
 * @author wayshall
 * <br/>
 */
public interface MvcInterceptor {
	
	/****
	 * 
	 * @author wayshall
	 * @param request
	 * @param response
	 * @param handler
	 * @return 
	 */
	boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) ;

	void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) ;
	
	void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) ;
	
	/****
	 * async controller will invoke
	 * @author wayshall
	 * @param request
	 * @param response
	 * @param handler
	 */
	void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler);
	
}
