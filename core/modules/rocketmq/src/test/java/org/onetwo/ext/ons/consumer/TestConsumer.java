package org.onetwo.ext.ons.consumer;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.onetwo.common.date.DateUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.annotation.ONSSubscribe.IdempotentType;
import org.onetwo.ext.ons.producer.ONSProducerTest;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerDelayMessageTest;


/**
 * @author wayshall
 * <br/>
 */
@ONSSubscribe(consumerId="${consumerIds.test1}", 
			topic=ONSProducerTest.TOPIC, 
			tags={ONSProducerTest.ORDER_CANCEL, ONSProducerTest.ORDER_PAY}, 
			consumeFromWhere=ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET,
			idempotent=IdempotentType.DATABASE)
public class TestConsumer implements CustomONSConsumer /*MessageListener*/ {
	int count = 0;

	@Override
	public void doConsumeBatch(List<ConsumContext> batchContexts) {
	}

	//	@Override
	public void doConsume(ConsumContext consumContext) {
		/*if(count<1){
			count++;
			throw new ServiceException("抛错");
		}*/
		System.out.println(this.getClass().getName() +" " + DateUtils.formatDateTime(new Date()) + ": 收到消息：" + consumContext.getDeserializedBody());
		if (RmqONSProducerDelayMessageTest.messages.remove(consumContext.getDeserializedBody())) {
			RmqONSProducerDelayMessageTest.delayCountDownLatch.countDown();
		}
	}

}
