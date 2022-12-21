package org.onetwo.cloud.webmgr;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnClass(AbstractEndpoint.class)
public class CloudManagementConfiguration {
	
	@Bean
	public RefreshEndpointWebCommand refreshEndpointWebCommand() {
		return new RefreshEndpointWebCommand();
	}

}
