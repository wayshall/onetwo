package org.onetwo.boot.plugin.mvc;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class PluginWebMvcRegistrations extends WebMvcRegistrationsAdapter {

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new BootPluginRequestMappingHandlerMapping();
	}

}
