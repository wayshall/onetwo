package org.onetwo.boot.module.jms;

import org.onetwo.boot.mq.cosume.ReceiveMessageRepository;
import org.onetwo.boot.mq.exception.MessageConsumedException;
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
public class JmsConsumeMessageStoreService {
	@Autowired(required=false)
	private ReceiveMessageRepository receiveMessageRepository;

	
	public void save(JmsConsumeContext context) {
		String msgId = context.getMessageId();
		try {
//			this.receiveMessageRepository.save(consumerMeta, context);
			this.receiveMessageRepository.save(context.getMessageKey(), context.getMessageId(), context.getConsumeGroup());
		} catch (DuplicateKeyException e) {
			throw new MessageConsumedException("msgId: " + msgId + 
					", msgkey: " + context.getMessageKey() + 
					", consume group: " + context.getConsumeGroup() +
					", store error: " + e.getMessage(), 
					e);
		}
	}
	

}

