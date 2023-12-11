package org.onetwo.boot.module.artemis.consumer;

import org.onetwo.boot.module.jms.JmsMessage;
import org.onetwo.boot.module.jms.JmsUtils.ContainerFactorys;
import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.onetwo.boot.module.jms.annotation.JmsConsumer;
import org.onetwo.boot.module.jms.exception.ConsumeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@JmsConsumer
@Slf4j
public class ArtemisGroup2JmsConsumerBean {


	@IdempotentListener(destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group2")
	public void cosumerAOfGroup2(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerA@gourp2 ConsumerTestBean receive topic message: "+msg.getBody());
		throw new ConsumeException("error consume on group2");
	}

	@IdempotentListener(destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group2")
	public void cosumerBOfGroup2(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerB@gourp2 ConsumerTestBean receive topic message: "+msg.getBody());
		throw new ConsumeException("error consume on group2");
	}

}
