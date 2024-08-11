package org.onetwo.ext.ons.consumer;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.producer.ONSProducerTest;


/**
 * @author wayshall
 * <br/>
 */
@ONSConsumer
public class AnnotaionConsumer  {

	@ONSSubscribe(consumerId="${consumerIds.test2}", topic=ONSProducerTest.TOPIC, tags=ONSProducerTest.ORDER_PAY, consumeFromWhere=ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET)
	public void doConsume(ConsumContext consumContext, String body) {
		System.out.println("注解消费者，收到消息：" + consumContext.getDeserializedBody());
	}

}
