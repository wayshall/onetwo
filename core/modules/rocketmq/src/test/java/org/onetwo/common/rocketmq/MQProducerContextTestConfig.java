package org.onetwo.common.rocketmq;

import org.onetwo.common.rocketmq.producer.RocketMQProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes=MQProducerContextTestConfig.class)
@PropertySource(value="classpath:config.properties")
public class MQProducerContextTestConfig {
	public static final String MQ_PRODUCER_GROUP_INDEX_UPDATE = "search-producer-index-update";
	
	@Value("${mq.namesrvAddr}")
	private String namesrvAddr;
	
	@Bean
	public RocketMQProducerService indexUpdateProducer(){
		RocketMQProducerService producer = new RocketMQProducerService();
		producer.setGroupName(MQ_PRODUCER_GROUP_INDEX_UPDATE);
		producer.setNamesrvAddr(namesrvAddr);
		return producer;
	}

}
