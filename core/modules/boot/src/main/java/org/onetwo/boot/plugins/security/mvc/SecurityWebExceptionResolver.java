package org.onetwo.boot.plugins.security.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.common.exception.AuthenticationException;
import org.springframework.web.method.HandlerMethod;

public class SecurityWebExceptionResolver extends BootWebExceptionResolver {

	@Override
	protected void processAfterLog(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex){
		if(ex instanceof AuthenticationException){
			throw new org.springframework.security.authentication.BadCredentialsException(ex.getMessage());
		}
	}
}
