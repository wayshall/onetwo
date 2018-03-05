package org.onetwo.boot.module.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author wayshall
 * <br/>
 */
@Component
public class ConsumerTestBean {

	@JmsListener(destination = "sample.queue")
	public void receiveQueue(String text) {
		System.out.println(text);
	}

	@JmsListener(destination = "sample.replyQueue")
	public String receiveReplyQueue(String text) {
		System.out.println(text);
		return "I got it!";
	}
}
