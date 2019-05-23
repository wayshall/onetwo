package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.annotation.ONSSubscribe.IdempotentType;
import org.onetwo.ext.ons.producer.ONSProducerTest;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;


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

//	@Override
	public void doConsume(ConsumContext consumContext) {
		/*if(count<1){
			count++;
			throw new ServiceException("抛错");
		}*/
		System.out.println("收到消息：" + consumContext.getDeserializedBody());
	}

//	@Override
	public Action consume(Message message, ConsumeContext context) {
		System.out.println("收到消息：" + message.getMsgID());
		return Action.CommitMessage;
	}

}
