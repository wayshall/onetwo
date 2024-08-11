package org.onetwo.ext.rocketmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductPutawayTestAppMQConsumer implements AppMQConsumer<Long> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public ConsumerMeta getConsumerMeta() {
		return new ConsumerMeta("search-consumer-product-putaway-test", 
								"product", 
								"index-update");
	}

	@Override
	public void doConsume(Long productId) {
		logger.info("receive productId: {}", productId);
	}

}
