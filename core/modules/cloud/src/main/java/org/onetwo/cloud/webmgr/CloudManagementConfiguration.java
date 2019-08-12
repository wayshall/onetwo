package org.onetwo.cloud.webmgr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
public class CloudManagementConfiguration {
	
	@Bean
	public RefreshEndpointWebCommand refreshEndpointWebCommand() {
		return new RefreshEndpointWebCommand();
	}

}
