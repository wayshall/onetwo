package org.onetwo.ext.rmqwithonsclient.producer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.dbm.id.SnowflakeIdGenerator;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.producer.RmqProducerService;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.OrderTestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DataBaseProducerServiceImpl {

	@Autowired
	RmqProducerService onsProducerService;
	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(30);
	

	public OrderTestMessage sendDelayMessage(Date deliverTime){
		Long id = idGenerator.nextId();
		OrderTestMessage msg = OrderTestMessage.builder()
			.orderId(id)
			.title("取消-"+DateUtils.formatDateTime(deliverTime))
			.build();
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
				  .topic(RmqONSProducerTest.TOPIC)
				  .tags(RmqONSProducerTest.ORDER_CANCEL)
				  .key("test_"+id)
				  .deliverAt(deliverTime)
				  .body(msg)
				  .build(), SendMessageFlags.EnableDatabaseTransactional);
		
		System.out.println("res: " + res);
		assertThat(res).isEqualTo(ONSUtils.ONS_SUSPEND);
		return msg;
	}

	public void sendMessage(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .key("test_"+idGenerator.nextId())
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build());
		System.out.println("res: " + res);
		ONSUtils.checkSendResult(res);
		
		SendResult sendResult = onsProducerService.sendMessage(
				SimpleMessage.builder()
				  .topic(RmqONSProducerTest.TOPIC) // 消息主题
				  .tags(RmqONSProducerTest.ORDER_CANCEL) // 消息标签
				  .userId("100") // 表示订单用户
				  .dataId("2") // 表示订单id
				  .occurOn(new Date()) //消息发送时间，可不写，默认为当前
				  // body以外的属性属于发送时需要的属性，比如上面三个属性主要是用于生成消息的唯一key，
				  // 如果自己有设置key属性，则不需要；这些属性暂时不会发送到客户端
				  .body( // 这里才是实际要发送的，客户端收到的消息体
						  OrderTestMessage.builder()
							  				.orderId(2L)
							  				.title("取消")
							  				.build()
				  ).build(), 
				  SendMessageFlags.EnableDatabaseTransactional);// 启动消息本地事务，以保持一致性
		
		System.out.println("sendResult: " + sendResult);
		assertThat(sendResult).isEqualTo(ONSUtils.ONS_SUSPEND);
	}

	public void sendMessageWithException(){
		SendResult res = onsProducerService.sendMessage(SimpleMessage.builder()
																	  .topic(RmqONSProducerTest.TOPIC)
																	  .tags(RmqONSProducerTest.ORDER_PAY)
																	  .key("test_"+idGenerator.nextId())
																	  .body(OrderTestMessage.builder()
																			  				.orderId(1L)
																			  				.title("支付")
																			  				.build())
																	  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		res = onsProducerService.sendMessage(SimpleMessage.builder()
				  .topic(RmqONSProducerTest.TOPIC)
				  .tags(RmqONSProducerTest.ORDER_CANCEL)
				  .key("test_"+idGenerator.nextId())
				  .body(OrderTestMessage.builder()
			  				.orderId(1L)
			  				.title("取消")
			  				.build())
				  .build(), SendMessageFlags.EnableDatabaseTransactional);
		System.out.println("res: " + res);
		
		assertThat(res).isEqualTo(ONSUtils.ONS_SUSPEND);
		assertThat(res.getMsgId()).isEqualTo(ONSUtils.ONS_SUSPEND.getMsgId());
		
		if(true){
			throw new ServiceException("发消息后出错！");
		}
	}
}
