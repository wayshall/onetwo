package org.onetwo.ext.ons.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.ons.producer.ONSProducerTest.ProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ONSProducerTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "${producerId}";
	public static final String ORDER_PAY = "${tags.orderPay}";
	public static final String ORDER_CANCEL = "${tags.orderCancel}";

	@Autowired
	ProducerService onsProducerService;
	
	@Test
	public void testSendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(TOPIC)
																	  .tags(ORDER_PAY)
																	  .key("1")
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build());
		System.out.println("res: " + res);
		
//		res = onsProducerService.sendMessage(SimpleMessage.builder()
//				  .topic(TOPIC)
//				  .tags(ORDER_CANCEL)
//				  .key("1")
//				  .body(OrderTestMessage.builder()
//			  				.orderId(1L)
//			  				.title("取消")
//			  				.build())
//				  .build());
//		System.out.println("res: " + res);
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@EnableONSClient(producers=@ONSProducer(producerId=PRODUER_ID))
	@Configuration
	@PropertySource("classpath:ons.properties")
	public static class ProducerTestContext {
	}
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderTestMessage {
		Long orderId;
		String title;
	}
}
