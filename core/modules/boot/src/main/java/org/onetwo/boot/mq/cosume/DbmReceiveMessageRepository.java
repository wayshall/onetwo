package org.onetwo.boot.mq.cosume;

import org.onetwo.boot.mq.entity.ReceiveMessageEntity;
import org.onetwo.boot.mq.entity.ReceiveMessageEntity.ConsumeStates;
import org.onetwo.common.db.spi.BaseEntityManager;
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

