package org.onetwo.common.rocketmq;

import org.onetwo.common.rocketmq.consumer.AppMQConsumer;
import org.onetwo.common.rocketmq.consumer.ConsumerMeta;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.rocketmq.common.message.MessageExt;

@Transactional
public class ProductPutawayTestAppMQConsumer implements AppMQConsumer<Long> {

	
	@Override
	public ConsumerMeta getConsumerMeta() {
		return new ConsumerMeta("search-consumer-product-putaway-test", 
								"product", 
								"index-update");
	}

	@Override
	public void doConsume(MessageExt msg, Long productId) {
	}

}
