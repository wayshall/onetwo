package org.onetwo.ext.ons.consumer;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;


public interface CustomConsumer<T> {

	default T deserialize(MessageExt msg) {
		return SerializationUtils.deserialize(msg.getBody());
	}
	
	void doConsume(List<T> body);
	
}
