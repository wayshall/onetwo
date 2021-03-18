package org.onetwo.boot.module.activemq.jmx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(ActiveMQJmxServiceProperties.class)
@ConditionalOnProperty(ActiveMQJmxServiceProperties.ENABLE_KEY)
public class ActiveMQJmxConfiguration {
	
	@Bean
	public ActiveMQJMXService activeMQJMXService() {
		ActiveMQJMXService activeMQJMXService = new ActiveMQJMXService();
		return activeMQJMXService;
	}

}
