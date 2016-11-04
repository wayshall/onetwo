package org.onetwo.boot.module.security.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.ModelAndView;

public class SecurityWebExceptionResolver extends BootWebExceptionResolver {

	@Autowired(required=false)
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, Exception ex) {
		if(ex instanceof AuthenticationException){
			if(authenticationFailureHandler!=null){
				try {
					authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException(ex.getMessage(), ex));
					//处理后返回空的mv，如果返回null，dispatcher会认为异常没有被处理，抛出ex，见processHandlerException
					return new ModelAndView();
				} catch (Exception e) {
					throw new BaseException("handle authentication failure error: " + e.getMessage(), e);
				}
			}else{
				throw new org.springframework.security.authentication.BadCredentialsException(ex.getMessage());
			}
		}
		return super.doResolveException(request, response, handlerMethod, ex);
	}
}
