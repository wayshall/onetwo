package org.onetwo.cloud.env.rmq;

import org.onetwo.ext.ons.ONSProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnClass(ONSProperties.class)
//@ConditionalOnBean(ONSProducerServiceImpl.class) // not work
@ConditionalOnProperty(value = ONSProperties.ONS_ENABLE_KEY)
public class RmqAuthEnvsConfiguration {
	
	@Bean
	public AuthEnvRmqMessageInterceptor authEnvRmqMessageInterceptor() {
		return new AuthEnvRmqMessageInterceptor();
	}
	
	@Bean
	public AuthEnvRmqConsumerListener authEnvRmqConsumerListener() {
		return new AuthEnvRmqConsumerListener();
	}
	
}
