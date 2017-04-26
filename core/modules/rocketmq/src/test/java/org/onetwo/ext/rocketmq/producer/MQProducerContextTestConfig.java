package org.onetwo.ext.rocketmq.producer;

import org.onetwo.ext.rocketmq.annotation.EnableRocketMQ;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes=MQProducerContextTestConfig.class)
@PropertySource(value="classpath:config.properties")
@EnableRocketMQ
public class MQProducerContextTestConfig {
	public static final String MQ_PRODUCER_GROUP_INDEX_UPDATE = "search-producer-index-update";
	
}
