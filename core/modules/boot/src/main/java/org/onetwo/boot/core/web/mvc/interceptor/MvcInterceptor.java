package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * 和spring 拦截器的接口基本一样，重新定义只是为了区分
 * @author wayshall
 * <br/>
 */
public interface MvcInterceptor {
	
	void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) ;

	void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) ;
	
	void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) ;
	
}
