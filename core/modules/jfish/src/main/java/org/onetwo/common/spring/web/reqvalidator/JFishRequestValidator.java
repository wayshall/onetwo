package org.onetwo.common.spring.web.reqvalidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

public interface JFishRequestValidator {
	
	public boolean isSupport(HttpServletRequest request, HandlerMethod handler);

	public void doValidate(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler);

}