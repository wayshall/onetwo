package org.onetwo.common.rocketmq;

import org.onetwo.common.rocketmq.consumer.RocketMQPushConsumerStarter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes=MQProducerContextTestConfig.class)
@PropertySource(value="classpath:config.properties")
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
	
	@Bean
	public RocketMQPushConsumerStarter rocketMQPushConsumerStarter(){
		RocketMQPushConsumerStarter starter = new RocketMQPushConsumerStarter();
		return starter;
	}

}
