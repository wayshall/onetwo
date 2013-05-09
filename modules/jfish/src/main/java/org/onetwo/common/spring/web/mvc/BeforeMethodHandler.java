package org.onetwo.common.spring.web.mvc;

import java.lang.reflect.Method;

public interface BeforeMethodHandler {

	public void beforeHandler(Method handleMethod);
}
