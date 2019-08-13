package org.onetwo.cloud.eureka;

import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@EnableDiscoveryClient
@EnableConfigurationProperties({BootJfishCloudConfig.class})
@Configuration
public class EurekaClientContextConfig {

	@Bean
	@ConditionalOnBean(BootServletContextInitializer.class)
	public EurekaUnregisterProcessor eurekaUnregisterProcessor() {
		return new EurekaUnregisterProcessor();
	}

	public EurekaClientContextConfig(){
	}
}
