package org.onetwo.plugins.activemq.consumer;

import org.onetwo.plugins.activemq.test.ActivemqSiteConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivemqConsumerTestContext {

	@Bean
	public ActivemqSiteConfig appConfig(){
		return ActivemqSiteConfig.getInstance();
	}
	
	@Bean
	public ConsumerServiceImpl ConsumerServiceImpl(){
		return new ConsumerServiceImpl();
	}
}
