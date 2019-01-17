package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.entity.ReceiveMessageEntity;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ReceiveMessageRepository {
	
	ReceiveMessageEntity save(ConsumerMeta consumerMeta, ConsumContext context);

}

