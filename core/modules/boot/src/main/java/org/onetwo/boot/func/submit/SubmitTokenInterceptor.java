package org.onetwo.boot.func.submit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author weishao zeng
 * <br/>
 */
public class SubmitTokenInterceptor implements MvcInterceptor {
	
	@Autowired
	private SubmitTokenService submitTokenService;
	private String tokenParameterName = "ticket";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		String ticket = request.getParameter(tokenParameterName);
		this.submitTokenService.check(ticket);
		return true;
	}

}

