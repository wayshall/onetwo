package org.onetwo.ext.rocketmq.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.rocketmq.consumer.RocketMQPushConsumerStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


//@ContextConfiguration("classpath:applicationContext-mq-es-test.xml")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=MQConsumerContextTestConfig.class)
public class MQConsumerTest  {
	
	@Autowired
	private RocketMQPushConsumerStarter starter;
	@Test
	public void test() throws Exception{
		System.out.println("starting...");
		System.in.read();
		starter.destroy();
	}

}
