package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wayshall
 * <br/>
 */
public class MvcInterceptorAdapter implements MvcInterceptor, Ordered {
	
	public static final int ORDER_FIRST = Ordered.HIGHEST_PRECEDENCE + 100000000;
	public static final int ORDER_STEP = 10;
	public static final int ORDER_DEFAULT = 0;
	
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

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
	}

	@Override
	public int getOrder() {
		return ORDER_DEFAULT;
	}
	
	protected int afterFirst(int step) {
		return ORDER_FIRST + step;
	}
	
	protected int afterDefault(int step) {
		return ORDER_DEFAULT + step;
	}
	
	protected int beforeDefault(int step) {
		return ORDER_DEFAULT - step;
	}

}
