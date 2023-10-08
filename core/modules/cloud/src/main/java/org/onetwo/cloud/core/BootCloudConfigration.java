package org.onetwo.cloud.core;

import org.onetwo.boot.core.web.mvc.ExtRequestMappingHandlerMapping;
import org.onetwo.cloud.bugfix.FixFeignClientsHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class BootCloudConfigration {

//	@Bean
//	@ConditionalOnClass(name={BootCloudUtils.FEIGN_CLIENT_CLASS_NAME, BootCloudUtils.FEIGN_CLASS_NAME})
//	public BootWebMvcRegistrations bootWebMvcRegistrations(){
//		return new CloudWebMvcRegistrations();
//	}

	@Bean
	@Primary
	public ExtRequestMappingHandlerMapping fixFeignClientsHandlerMapping() {
		return new FixFeignClientsHandlerMapping();
	}
	
}
