package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.annotation.ONSSubscribe;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;


/**
 * @author wayshall
 * <br/>
 */
@ONSSubscribe(consumerId="CID_LEGO_ORDER", topic="lego-order", subExpression="order-pay", consumeFromWhere=ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET)
public class TestConsumer implements CustomONSConsumer<String> /*MessageListener*/ {

//	@Override
	public void doConsume(ConsumContext consumContext) {
		System.out.println("收到消息：" + consumContext.getDeserializedBody());
	}

//	@Override
	public Action consume(Message message, ConsumeContext context) {
		System.out.println("收到消息：" + message.getMsgID());
		return Action.CommitMessage;
	}


	

}
