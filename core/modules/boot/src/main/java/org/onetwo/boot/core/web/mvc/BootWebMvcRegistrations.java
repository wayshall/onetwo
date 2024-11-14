package org.onetwo.boot.core.web.mvc;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class BootWebMvcRegistrations extends WebMvcRegistrationsAdapter {

	@Override
	public ExtRequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ExtRequestMappingHandlerMapping();
	}


	@Override
	public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
		return null;
	}
}
