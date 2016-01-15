package org.onetwo.common.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

public class SimpleCharacterEncodingFilter extends CharacterEncodingFilter implements Ordered {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private int order = Ordered.HIGHEST_PRECEDENCE;
	
	public SimpleCharacterEncodingFilter(){
		this.setEncoding("UTF-8");
		this.setForceEncoding(true);
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("request: {}", request.getClass());
		ServletRequest req = request;
		while(req instanceof HttpServletRequestWrapper){
			HttpServletRequestWrapper reqWrapper = (HttpServletRequestWrapper) req;
			req = reqWrapper.getRequest();
			logger.info("req: {}", req.getClass());
		}
		logger.info("=================>>>> set encoding: {}, {}", request.getCharacterEncoding(), request.getRequestURL());
		super.doFilterInternal(request, response, filterChain);;
		logger.info("=================>>>> after set encoding: {}", request.getCharacterEncoding());
		if("true".equalsIgnoreCase(request.getParameter("debug"))){
			RuntimeException re = new RuntimeException();
			logger.error("SimpleCharacterEncodingFilter debug", re);
		}
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
