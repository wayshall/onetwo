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
import org.springframework.web.filter.OncePerRequestFilter;

public class SimpleCharacterEncodingFilter extends OncePerRequestFilter implements Ordered {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private String encoding;
	private boolean forceEncoding = false;
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
		ServletRequest req = getNativeRequest(request);
		logger.info("=================>>>> set encoding: {}, {}", req.getCharacterEncoding(), request.getRequestURL());
		
		if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
			req.setCharacterEncoding(this.encoding);
			if (this.forceEncoding) {
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);

		logger.info("=================>>>> req after set encoding: {}", req.getCharacterEncoding());
		logger.info("=================>>>> request after set encoding: {}", request.getCharacterEncoding());
		
		if("true".equalsIgnoreCase(request.getParameter("debug"))){
			RuntimeException re = new RuntimeException();
			logger.error("SimpleCharacterEncodingFilter debug", re);
		}
	}
	
	private ServletRequest getNativeRequest(HttpServletRequest request){
		ServletRequest req = request;
		while(req instanceof HttpServletRequestWrapper){
			HttpServletRequestWrapper reqWrapper = (HttpServletRequestWrapper) req;
			req = reqWrapper.getRequest();
			logger.info("req: {}", req.getClass());
		}
		return req;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}
	
}
