package org.onetwo.ext.alimq;

import org.apache.commons.lang3.SerializationUtils;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;


public interface MessageDeserializer {
	
	Object deserialize(byte[] body, MessageExt message);

	MessageDeserializer DEFAULT = (body, message)->SerializationUtils.deserialize(body);
}
