package org.onetwo.plugins.activemq.producer;

import org.onetwo.plugins.activemq.test.ActivemqSiteConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivemqProducerTestContext {

	@Bean
	public ActivemqSiteConfig appConfig(){
		return ActivemqSiteConfig.getInstance();
	}
	
	@Bean
	public ProducerServiceImpl producerServiceImpl(){
		return new ProducerServiceImpl();
	}
}
