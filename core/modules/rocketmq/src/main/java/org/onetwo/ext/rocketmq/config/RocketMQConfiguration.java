package org.onetwo.ext.rocketmq.config;

import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.ext.rocketmq.consumer.RocketMQPushConsumerStarter;
import org.onetwo.ext.rocketmq.producer.RocketMQProducerService;
import org.onetwo.ext.rocketmq.transaction.GenericTransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Bean
	@Autowired
	public GenericTransactionListener rmqTransactionListener(SendMessageRepository sendMessageRepository, MessageBodyStoreSerializer serializer) {
		GenericTransactionListener listener = new GenericTransactionListener();
		return listener;
	}
}
