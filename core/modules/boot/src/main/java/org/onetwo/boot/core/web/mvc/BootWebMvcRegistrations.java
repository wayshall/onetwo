package org.onetwo.boot.core.web.mvc;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;

/***
 * 
 * @author wayshall
 *
 */
public class BootWebMvcRegistrations  /* extends WebMvcRegistrationsAdapter*/ implements WebMvcRegistrations {

	@Override
	public ExtRequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ExtRequestMappingHandlerMapping();
	}

}
