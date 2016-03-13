package org.onetwo.boot.plugins.security.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.method.HandlerMethod;

public class SecurityWebExceptionResolver extends BootWebExceptionResolver {

	@Autowired(required=false)
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void processAfterLog(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex){
		if(ex instanceof AuthenticationException){
			if(authenticationFailureHandler!=null){
				try {
					authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException(ex.getMessage(), ex));
				} catch (Exception e) {
					throw new BaseException("handle authentication failure error: " + e.getMessage(), e);
				}
			}else{
				throw new org.springframework.security.authentication.BadCredentialsException(ex.getMessage());
			}
		}
	}
}
