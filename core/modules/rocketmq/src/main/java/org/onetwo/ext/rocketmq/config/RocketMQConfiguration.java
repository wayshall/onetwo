package org.onetwo.ext.rocketmq.config;

import org.onetwo.ext.rocketmq.consumer.RocketMQPushConsumerStarter;
import org.onetwo.ext.rocketmq.producer.RocketMQProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
public class RocketMQConfiguration {

	@Value("${rocketmq.namesrvAddr:#{null}}")
	private String namesrvAddr;
	@Value("${rocketmq.defaultProduerGroup:defaultProduerGroup}")
	private String defaultProduerGroup;
	
	@Bean
	public RocketMQPushConsumerStarter rocketMQPushConsumerStarter(){
		RocketMQPushConsumerStarter starter = new RocketMQPushConsumerStarter();
		starter.setNamesrvAddr(namesrvAddr);
		return starter;
	}
	
	@Bean
	public RocketMQProducerService rocketMQProducerService(){
		RocketMQProducerService producer = new RocketMQProducerService();
		producer.setGroupName(defaultProduerGroup);
		producer.setNamesrvAddr(namesrvAddr);
		return producer;
	}
}
