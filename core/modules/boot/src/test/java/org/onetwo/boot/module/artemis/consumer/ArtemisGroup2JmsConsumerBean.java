package org.onetwo.boot.module.artemis.consumer;

import org.onetwo.boot.module.jms.JmsMessage;
import org.onetwo.boot.module.jms.JmsUtils.ContainerFactorys;
import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.onetwo.boot.module.jms.annotation.JmsConsumer;
import org.onetwo.boot.mq.IdempotentType;
import org.onetwo.common.exception.MessageOnlyServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@JmsConsumer
@Slf4j
public class ArtemisGroup2JmsConsumerBean {


	@IdempotentListener(idempotentType = IdempotentType.DATABASE, destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group2")
	public void cosumerAOfGroup2(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerA@gourp2 ConsumerTestBean receive topic message: "+msg.getBody());
		throw new MessageOnlyServiceException("error consume on group2");
	}

	@IdempotentListener(idempotentType = IdempotentType.DATABASE, destination = "sample.topic", containerFactory=ContainerFactorys.TOPIC, subscription="group2")
	public void cosumerBOfGroup2(JmsMessage<String> msg) {
		System.out.println(msg.getBody());
		log.info("consumerB@gourp2 ConsumerTestBean receive topic message: "+msg.getBody());
		throw new MessageOnlyServiceException("error consume on group2");
	}

}
