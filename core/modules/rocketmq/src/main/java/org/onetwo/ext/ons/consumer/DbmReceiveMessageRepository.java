package org.onetwo.ext.ons.consumer;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.ext.ons.entity.ReceiveMessageEntity;
import org.onetwo.ext.ons.entity.ReceiveMessageEntity.ConsumeStates;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class DbmReceiveMessageRepository implements ReceiveMessageRepository {
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Override
	public ReceiveMessageEntity save(String msgKey, String msgId, String consumerGroup) {
		ReceiveMessageEntity entity = new ReceiveMessageEntity();
		entity.setMsgkey(msgKey);
		entity.setRawMsgid(msgId);
		entity.setState(ConsumeStates.CONSUMED);
		entity.setConsumeGroup(consumerGroup);
		baseEntityManager.persist(entity);
		return entity;
	}

}

