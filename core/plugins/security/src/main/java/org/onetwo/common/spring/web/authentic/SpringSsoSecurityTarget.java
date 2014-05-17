package org.onetwo.common.spring.web.authentic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.SessionStorer;
import org.springframework.web.method.HandlerMethod;

public class SpringSsoSecurityTarget extends SpringSecurityTarget {

	public SpringSsoSecurityTarget(SessionStorer sessionStorer, HttpServletRequest request, HttpServletResponse response) {
		super(sessionStorer, request, response);
	}

	public SpringSsoSecurityTarget(SessionStorer sessionStorer, HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		super(sessionStorer, request, response, handler);
	}

	@Override
	public String getSessionKey() {
		return super.getCookieToken();
	}

}
