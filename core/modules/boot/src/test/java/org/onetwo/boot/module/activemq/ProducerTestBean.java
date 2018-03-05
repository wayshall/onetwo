package org.onetwo.boot.module.activemq;

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
	/*@Autowired
	private Queue queue;*/
	
	public void send(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
		this.jmsMessagingTemplate.convertAndSend("sample.queue", msg);
	}
	
	public String sendReplyQueue(String msg){
//		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
//		GenericMessage<String> message = new GenericMessage<>(msg);
		String receive = this.jmsMessagingTemplate.convertSendAndReceive("sample.replyQueue", msg, String.class);
		return receive;
	}
}
