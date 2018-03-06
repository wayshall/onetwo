package org.onetwo.boot.module.activemq;

import org.onetwo.boot.module.jms.JmsMessage;
import org.onetwo.boot.mq.ProducerService;
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
	private ProducerService<JmsMessage, Object> producerService;
	/*@Autowired
	private Queue queue;*/
	
	public void send(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
		String destinationName = "sample.queue";
//		this.jmsMessagingTemplate.convertAndSend(destinationName, msg);
		this.producerService.sendMessage(ActivemqMessage.builder()
														.destinationName(destinationName)
														.messageBody(msg)
														.build());
	}
	
	public String sendReplyQueue(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
//		GenericMessage<String> message = new GenericMessage<>(msg);
		String destinationName = "sample.queue";
		String receive = this.jmsMessagingTemplate.convertSendAndReceive(destinationName, msg, String.class);
		return receive;
	}
}
