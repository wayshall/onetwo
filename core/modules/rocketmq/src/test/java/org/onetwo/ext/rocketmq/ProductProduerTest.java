package org.onetwo.ext.rocketmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.rocketmq.producer.RocketMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@ContextConfiguration("classpath:applicationContext-mq-test.xml")
@ContextConfiguration(classes=MQProducerContextTestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductProduerTest {
	
	@Autowired
	private RocketMQProducerService rocketMQProducerService;
	
	@Test
	public void testSendMessage(){
		rocketMQProducerService.sendMessage("product", "index-update", 2L);
//		rocketMQProducerService.sendMessage(MQTopic.PRODUCT.name(), MQTag.UPDATE_INDEX.name(), 2L);
	}
	

}
