package org.onetwo.ext.rocketmq.consumer;

import org.apache.commons.lang3.SerializationUtils;

import com.alibaba.rocketmq.common.message.MessageExt;

public interface AppMQConsumer<T> {

	public ConsumerMeta getConsumerMeta();
	default public T convertMessage(MessageExt msg){
		return SerializationUtils.deserialize(msg.getBody());
	}
	public void doConsume(T body);

}
