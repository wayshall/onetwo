package org.onetwo.cloud.core;

import org.onetwo.boot.core.web.mvc.BootWebMvcRegistrations;
import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping;
import org.onetwo.cloud.bugfix.FixFeignClientsHandlerMapping;

/**
 * @author wayshall
 * <br/>
 */
public class CloudWebMvcRegistrations extends BootWebMvcRegistrations {

	@Override
	public ExtRequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new FixFeignClientsHandlerMapping();
	}
	
}
