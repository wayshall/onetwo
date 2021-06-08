package org.onetwo.cloud.core;

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
	public CloudWebMvcRegistrations bootWebMvcRegistrations(){
		return new CloudWebMvcRegistrations();
	}
	
}
