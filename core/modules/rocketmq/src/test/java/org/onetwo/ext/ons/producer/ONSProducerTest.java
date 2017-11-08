package org.onetwo.ext.ons.producer;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.producer.ONSProducerService;
import org.onetwo.ext.ons.producer.ONSProducerTest.ProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ONSProducerTest {

	@Autowired
	ONSProducerService onsProducerService;
	
	@Test
	public void testSendMessage(){
		Message message = new Message("lego-order", "order-pay", "1", SerializationUtils.serialize("订单支付"));
		SendResult res = onsProducerService.send(message);
		System.out.println("res: " + res);
	}
	
	@EnableONSClient(producerIds="PID_LEGO_ORDER")
	@Configuration
	@PropertySource("classpath:ons.properties")
	public static class ProducerTestContext {
	}
}
