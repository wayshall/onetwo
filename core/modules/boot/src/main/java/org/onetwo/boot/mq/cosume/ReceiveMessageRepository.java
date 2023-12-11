package org.onetwo.boot.mq.cosume;

import org.onetwo.boot.mq.entity.ReceiveMessageEntity;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ReceiveMessageRepository {
	
	ReceiveMessageEntity save(String msgKey, String msgId, String consumerGroup);

}

