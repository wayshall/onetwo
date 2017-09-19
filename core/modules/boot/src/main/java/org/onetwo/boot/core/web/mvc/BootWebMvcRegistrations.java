package org.onetwo.boot.core.web.mvc;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;

public class BootWebMvcRegistrations extends WebMvcRegistrationsAdapter {

	@Override
	public ExtRequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ExtRequestMappingHandlerMapping();
	}

}
