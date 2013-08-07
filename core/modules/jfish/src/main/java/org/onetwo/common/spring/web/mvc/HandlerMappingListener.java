package org.onetwo.common.spring.web.mvc;

import java.util.Map;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public interface HandlerMappingListener {

	public void onHandlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods);
	
}
