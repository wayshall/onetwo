package org.onetwo.ext.rocketmq.consumer;

import org.onetwo.ext.rocketmq.annotation.EnableRocketMQ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

//@ContextConfiguration(classes=MQProducerContextTestConfig.class)
@PropertySource(value="classpath:config.properties")
@EnableRocketMQ
@Configuration
public class MQConsumerContextTestConfig {
	
	/*@Bean
	public IndexUpdateAppMQConsumer indexUpdateAppMQConsumer(){
		return new IndexUpdateAppMQConsumer();
	}*/
	@Bean
	public ProductPutawayTestAppMQConsumer productPutawayAppMQConsumer(){
		return new ProductPutawayTestAppMQConsumer();
	}
	@Bean
	public ProductTestAppMQConsumer productTestAppMQConsumer(){
		return new ProductTestAppMQConsumer();
	}
	
/*	@Bean
	public RocketMQPushConsumerStarter rocketMQPushConsumerStarter(){
		RocketMQPushConsumerStarter starter = new RocketMQPushConsumerStarter();
		return starter;
	}
*/
}
