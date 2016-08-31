package org.onetwo.ext.rocketmq;

import org.onetwo.ext.rocketmq.consumer.AppMQConsumer;
import org.onetwo.ext.rocketmq.consumer.ConsumerMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.rocketmq.common.message.MessageExt;

@Transactional
public class ProductTestAppMQConsumer implements AppMQConsumer<MessageExt> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public ConsumerMeta getConsumerMeta() {
		return new ConsumerMeta("search-consumer-product-test", 
								"product");
	}

	@Override
	public void doConsume(MessageExt msg) {
		logger.info("receive id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());
	}

}
