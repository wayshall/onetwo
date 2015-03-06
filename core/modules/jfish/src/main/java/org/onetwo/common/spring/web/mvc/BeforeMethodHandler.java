package org.onetwo.common.spring.web.mvc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public interface BeforeMethodHandler {

	public void beforeHandler(HttpServletRequest request, Method handleMethod);
}
