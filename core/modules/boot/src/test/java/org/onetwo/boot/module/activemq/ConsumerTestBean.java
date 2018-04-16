package org.onetwo.boot.module.activemq;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.module.jms.JmsMessage;
import org.onetwo.boot.module.jms.JmsUtils.ContainerFactorys;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author wayshall
 * <br/>
 */
@Component
@Slf4j
public class ConsumerTestBean {

	@JmsListener(destination = "sample.queue", subscription="ConsumerTestBean#receiveQueue", containerFactory=ContainerFactorys.TOPIC)
//	@JmsListener(destination = "sample.queue", subscription="ConsumerTestBean#receiveQueue")
	public void receiveQueue(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("ConsumerTestBean receive: "+msg.getBody());
	}

	@JmsListener(destination = "sample.replyQueue", subscription="ConsumerTestBean#receiveReplyQueue")
	public String receiveReplyQueue(String text) {
		System.out.println(text);
		return "I got it!";
	}
}
