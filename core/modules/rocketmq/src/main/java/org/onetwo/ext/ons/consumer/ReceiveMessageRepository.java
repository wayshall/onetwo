package org.onetwo.ext.ons.consumer;

import org.onetwo.ext.ons.entity.ReceiveMessageEntity;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ReceiveMessageRepository {

	ReceiveMessageEntity save(String msgKey, String msgId, String consumerGroup);

}

