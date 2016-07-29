package org.onetwo.common.rocketmq;

import org.junit.Test;
import org.onetwo.common.rocketmq.consumer.RocketMQPushConsumerStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


//@ContextConfiguration("classpath:applicationContext-mq-es-test.xml")
@ContextConfiguration(classes=MQProducerContextTestConfig.class)
public class MQConsumerTest  {
	
	@Autowired
	private RocketMQPushConsumerStarter starter;
	@Test
	public void test() throws Exception{
		System.in.read();
		starter.destroy();
	}

}
