package org.onetwo.ext.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.ext.rocketmq.annotation.RMQConsumer;
import org.onetwo.ext.rocketmq.annotation.RMQSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RMQConsumer
public class ProductTestAppMQConsumer{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RMQSubscribe(groupName="search-consumer-product-test", topic="product")
	public void doConsume(MessageExt msg) {
		logger.info("receive id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());
	}

}
