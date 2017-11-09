package org.onetwo.ext.ons.producer;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.ons.producer.ONSProducerTest.ProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ONSProducerTest {
	public static final String TOPIC = "ONETWO_TEST";

	@Autowired
	ProducerService onsProducerService;
	
	@Test
	public void testSendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(TOPIC)
																	  .tags("order-pay")
																	  .key("1")
																	  .body(SerializationUtils.serialize("订单支付"))
																	  .build());
		System.out.println("res: " + res);
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@EnableONSClient(producers=@ONSProducer(producerId="PID_ONETWO_TEST"))
	@Configuration
	@PropertySource("classpath:ons.properties")
	public static class ProducerTestContext {
	}
}
