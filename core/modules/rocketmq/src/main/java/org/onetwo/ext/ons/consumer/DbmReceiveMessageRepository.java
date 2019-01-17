package org.onetwo.ext.ons.consumer;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.ext.alimq.ConsumContext;
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
	public ReceiveMessageEntity save(ConsumerMeta consumerMeta, ConsumContext context) {
		ReceiveMessageEntity entity = new ReceiveMessageEntity();
		entity.setMsgkey(context.getMessage().getKeys());
		entity.setRawMsgid(context.getMessageId());
		entity.setState(ConsumeStates.CONSUMED);
		entity.setConsumeGroup(consumerMeta.getConsumerId());
		baseEntityManager.persist(entity);
		return entity;
	}

}

