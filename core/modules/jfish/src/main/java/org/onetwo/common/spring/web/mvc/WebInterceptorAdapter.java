package org.onetwo.common.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.MappedInterceptor;

abstract public class WebInterceptorAdapter implements HandlerInterceptor, Ordered {
	
	public static MappedInterceptor createMappedInterceptor(WebInterceptorAdapter webi){
		return new MappedInterceptor(webi.getPathPatterns(), webi);
	}
	
	public static final class InterceptorOrder {
		private static final int DIFFER = 100;
		
		public static final int FIRST = 0;
		public static final int SECURITY = afater(FIRST);
		public static final int MODEL_AND_VIEW_POST_PROCESS = 99999;
		
		private InterceptorOrder(){};
		
		public static int afater(int order){
			return order + DIFFER;
		}
		
		public static int before(int order){
			return order - DIFFER;
		}
	}

	private final String[] pathPatterns;

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

}
