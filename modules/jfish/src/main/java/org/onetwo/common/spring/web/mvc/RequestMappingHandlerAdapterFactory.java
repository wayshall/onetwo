package org.onetwo.common.spring.web.mvc;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public interface RequestMappingHandlerAdapterFactory {
	
	public RequestMappingHandlerAdapter createAdapter();

}
