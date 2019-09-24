package org.onetwo.cloud.eureka;

import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.cloud.core.BootJfishCloudConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.EurekaClient;

/**
 * @author wayshall
 * <br/>
 */
@EnableDiscoveryClient
@EnableConfigurationProperties({BootJfishCloudConfig.class})
@Configuration
@ConditionalOnProperty(name="eureka.client.enabled", matchIfMissing=true)
@ConditionalOnClass(EurekaClient.class)
public class EurekaClientContextConfig {

	@Bean
	@ConditionalOnBean({BootServletContextInitializer.class})
	public EurekaUnregisterProcessor eurekaUnregisterProcessor() {
		return new EurekaUnregisterProcessor();
	}

	public EurekaClientContextConfig(){
	}
}
