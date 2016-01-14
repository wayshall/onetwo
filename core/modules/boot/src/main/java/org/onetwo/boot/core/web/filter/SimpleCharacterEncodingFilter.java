package org.onetwo.boot.core.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.context.web.OrderedCharacterEncodingFilter;

public class SimpleCharacterEncodingFilter extends OrderedCharacterEncodingFilter {
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("request: {}", request.getClass());
		logger.info("=================>>>> set encoding: {}, {}", request.getCharacterEncoding(), request.getRequestURL());
		super.doFilterInternal(request, response, filterChain);;
		logger.info("=================>>>> after set encoding: {}", request.getCharacterEncoding());
	}
}
