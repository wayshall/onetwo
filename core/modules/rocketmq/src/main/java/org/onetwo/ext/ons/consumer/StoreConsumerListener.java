package org.onetwo.ext.ons.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.boot.mq.cosume.ReceiveMessageRepository;
import org.onetwo.boot.mq.exception.MessageConsumedException;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.ConsumerListener;
import org.onetwo.ext.ons.ONSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author weishao zeng
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Transactional
public class StoreConsumerListener implements ConsumerListener {
	@Autowired(required=false)
	private ReceiveMessageRepository receiveMessageRepository;

	@Override
	public void beforeConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
		if(consumerMeta.shouldWithTransational()) {
			store2Database(consumerMeta, context);
		}
	}
	
	private void store2Database(ConsumerMeta consumerMeta, ConsumContext context) {
		MessageExt message = context.getMessage();
		String msgId = ONSUtils.getMessageId(message);
		try {
//			this.receiveMessageRepository.save(consumerMeta, context);
			this.receiveMessageRepository.save(context.getMessage().getKeys(), context.getMessageId(), consumerMeta.getConsumerId());
		} catch (DuplicateKeyException e) {
			throw new MessageConsumedException("msgId: " + msgId + 
					", msgkey: " + message.getKeys() + 
					", consume group: " + consumerMeta.getConsumerId() +
					", store error: " + e.getMessage(), 
					e);
		}
	}
	
	@Override
	public void afterConsumeMessage(ConsumerMeta consumerMeta, ConsumContext context) {
	}

	@Override
	public void onConsumeMessageError(ConsumContext context, Throwable e) {
	}

}

