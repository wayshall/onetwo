package org.onetwo.boot.module.artemis.consumer;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.module.activemq.producer.ProducerTestBean.MessageBody;
import org.onetwo.boot.module.jms.JmsMessage;
import org.onetwo.boot.module.jms.JmsUtils.ContainerFactorys;
import org.onetwo.common.convert.Types;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author wayshall
 * <br/>
 */
@Component
@Slf4j
public class ArtemisTestBean {

	@JmsListener(destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group1")
	public void cosumerAOfGroup1(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerA ConsumerTestBean receive topic message: "+msg.getBody());
	}

	@JmsListener(destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group1")
	public void cosumerBOfGroup1(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerB ConsumerTestBean receive topic message: "+msg.getBody());
	}

	@JmsListener(destination = "sample.queue", containerFactory=ContainerFactorys.QUEUE)
//	@JmsListener(destination = "sample.queue", subscription="ConsumerTestBean#receiveQueue")
	public void receiveQueue(JmsMessage<MessageBody> msg) {
		System.out.println(msg.getBody().getMessage());
		Integer num = Types.asInteger(msg.getBody().getMessage());
		log.info("ConsumerTestBean receive: "+msg.getBody().getMessage());
		/*if(num%2==0){
			throw new BaseException("consume error"+msg.getBody().getMessage());
		}*/
	}

	@JmsListener(destination = "sample.replyQueue")
	public String receiveReplyQueue(String text) {
		System.out.println(text);
		return "I got it!";
	}
}
