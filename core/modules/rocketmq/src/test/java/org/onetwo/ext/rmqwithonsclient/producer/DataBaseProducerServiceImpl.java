package org.onetwo.ext.rmqwithonsclient.producer;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSUtils.SendMessageFlags;
import org.onetwo.ext.ons.producer.ProducerService;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.OrderTestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DataBaseProducerServiceImpl {

	@Autowired
	ProducerService onsProducerService;

	public void sendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		res = onsProducerService.sendMessage(SimpleMessage.builder()
				  .topic(RmqONSProducerTest.TOPIC)
				  .tags(RmqONSProducerTest.ORDER_CANCEL)
				  .body(OrderTestMessage.builder()
			  				.orderId(1L)
			  				.title("取消")
			  				.build())
				  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
	}

	public void sendMessageWithException(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		res = onsProducerService.sendMessage(SimpleMessage.builder()
				  .topic(RmqONSProducerTest.TOPIC)
				  .tags(RmqONSProducerTest.ORDER_CANCEL)
				  .body(OrderTestMessage.builder()
			  				.orderId(1L)
			  				.title("取消")
			  				.build())
				  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		if(true){
			throw new ServiceException("发消息后出错！");
		}
	}
}
