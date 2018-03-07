package org.onetwo.boot.module.activemq;

import org.onetwo.boot.module.jms.JmsMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author wayshall
 * <br/>
 */
@Component
public class ConsumerTestBean {

	@JmsListener(destination = "sample.queue")
	public void receiveQueue(JmsMessage<String> msg) {
		System.out.println(msg.getPayload());
	}

	@JmsListener(destination = "sample.replyQueue")
	public String receiveReplyQueue(String text) {
		System.out.println(text);
		return "I got it!";
	}
}
