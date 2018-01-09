package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.MappedInterceptor;

abstract public class WebInterceptorAdapter implements HandlerInterceptor, Ordered {
	public static final int FIRST = Ordered.HIGHEST_PRECEDENCE;
	public static final int LAST = Ordered.LOWEST_PRECEDENCE;
	public static final int NORMAL = 0;
	public static final int INCREMENTAL = 100;

	public static final int ORDERED_LOG = after(NORMAL);
	
	public static MappedInterceptor createMappedInterceptor(WebInterceptorAdapter webi){
		return new MappedInterceptor(webi.getPathPatterns(), webi);
	}
	
	public static int after(int order){
		int limit = LAST - INCREMENTAL;//防止溢出
		Assert.isTrue(order < limit, "error order: " + order);
		return order + INCREMENTAL;
	}
	
	public static int before(int order){
		int limit = FIRST+INCREMENTAL;
		Assert.isTrue(order > limit, "error order: " + order);
		return order - INCREMENTAL;
	}

	private String[] pathPatterns;

	public WebInterceptorAdapter(String[] pathPatterns) {
		this.pathPatterns = pathPatterns;
	}

	public WebInterceptorAdapter() {
		this.pathPatterns = null;
	}
	
	protected HandlerMethod getHandlerMethod(Object handler){
		if(handler instanceof HandlerMethod){
			return (HandlerMethod)handler;
		}
		return null;
	}
	
	public boolean isMethodHandler(Object handler){
		return getHandlerMethod(handler)!=null;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

	public String[] getPathPatterns() {
		return pathPatterns;
	}

	public void setPathPatterns(String[] pathPatterns) {
		this.pathPatterns = pathPatterns;
	}

}
