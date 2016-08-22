package org.onetwo.ext.security.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface LoginLogHandler {
	
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
	
	public void loginFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception);
	
	public void logoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
