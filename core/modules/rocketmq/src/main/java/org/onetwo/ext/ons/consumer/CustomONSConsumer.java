package org.onetwo.ext.ons.consumer;

import org.apache.commons.lang3.SerializationUtils;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;


public interface CustomONSConsumer<T> {

	default T deserialize(MessageExt msg) {
		return SerializationUtils.deserialize(msg.getBody());
	}
	
	void doConsume(MessageExt message, T body);
	
}
