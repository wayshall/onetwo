package org.onetwo.boot.module.activemq;

import java.util.Date;

import org.onetwo.boot.module.jms.JmsMessageCreator;
import org.onetwo.boot.module.jms.SimpleJmsMessageCreator;
import org.onetwo.boot.module.jms.JmsMessageCreator.DesinationType;
import org.onetwo.boot.mq.ProducerService;
import org.onetwo.common.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wayshall
 * <br/>
 */
@Component
public class ProducerTestBean {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private ProducerService<JmsMessageCreator, Object> producerService;
	/*@Autowired
	private Queue queue;*/

	public void send(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
		String destinationName = "sample.queue";
//		this.jmsMessagingTemplate.convertAndSend(destinationName, msg);
		this.producerService.sendMessage(SimpleJmsMessageCreator.builder()
														.destinationName(destinationName)
														.key("test_"+DateUtils.formatDateTimeMillis(new Date()))
														.body(msg)
														.build());
	}
	public void send2Topic(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
		String destinationName = "sample.queue";
//		this.jmsMessagingTemplate.convertAndSend(destinationName, msg);
		this.producerService.sendMessage(SimpleJmsMessageCreator.builder()
														.destinationName(destinationName)
														.key("test_"+DateUtils.formatDateTimeMillis(new Date()))
														.desinationType(DesinationType.TOPIC)
														.body(msg)
														.build());
	}
	
	public String sendReplyQueue(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
//		GenericMessage<String> message = new GenericMessage<>(msg);
		String destinationName = "sample.replyQueue";
		String receive = this.jmsMessagingTemplate.convertSendAndReceive(destinationName, msg, String.class);
		return receive;
	}
}
