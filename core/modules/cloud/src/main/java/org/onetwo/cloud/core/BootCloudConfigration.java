package org.onetwo.cloud.core;

import org.onetwo.boot.core.web.mvc.BootWebMvcRegistrations;
import org.onetwo.cloud.util.BootCloudUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class BootCloudConfigration {

	@Bean
	@ConditionalOnClass(name={BootCloudUtils.FEIGN_CLIENT_CLASS_NAME, BootCloudUtils.FEIGN_CLASS_NAME})
	public BootWebMvcRegistrations bootWebMvcRegistrations(){
		return new CloudWebMvcRegistrations();
	}

//	@Bean
//	@Primary
//	public ExtRequestMappingHandlerMapping fixFeignClientsHandlerMapping() {
//		return new FixFeignClientsHandlerMapping();
//	}
	
}
