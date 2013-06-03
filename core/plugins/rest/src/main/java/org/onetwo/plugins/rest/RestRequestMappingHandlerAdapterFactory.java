package org.onetwo.plugins.rest;

import org.onetwo.common.spring.web.mvc.RequestMappingHandlerAdapterFactory;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class RestRequestMappingHandlerAdapterFactory implements RequestMappingHandlerAdapterFactory {

	@Override
	public RequestMappingHandlerAdapter createAdapter() {
		return new RestRequestMappingHandlerAdapter();
	}

}
