package org.onetwo.cloud.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class BootCloudConfigration {

	@Bean
	public CloudWebMvcRegistrations bootWebMvcRegistrations(){
		return new CloudWebMvcRegistrations();
	}
}
