package org.onetwo.cloud.core;

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
	@ConditionalOnClass(name={"org.springframework.cloud.netflix.feign.FeignClient", "feign.Feign"})
	public CloudWebMvcRegistrations bootWebMvcRegistrations(){
		return new CloudWebMvcRegistrations();
	}
	
}
