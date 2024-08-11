package org.onetwo.ext.ons.producer;

import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.onetwo.ext.alimq.ExtMessage;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wayshall
 * <br/>
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ONSSendMessageContext extends org.onetwo.boot.mq.SendMessageContext<ExtMessage>{
	
//	final private Message message;
	final private TraceableProducer source;
//	final private ProducerBean producer;
//	final private TransactionProducerBean transactionProducer;
//	final private SendMessageInterceptorChain chain;
//	private SendMessageEntity messageEntity;
	/*
	final private long threadId;
	private boolean debug;*/

	@Builder
	public ONSSendMessageContext(ExtMessage message,
			SendMessageEntity messageEntity, long threadId,
			boolean debug, TraceableProducer source, 
			/*ProducerBean producer,
			TransactionProducerBean transactionProducer,*/
			SendMessageInterceptorChain chain) {
		super(message.getKeys(), message, chain, messageEntity, threadId, debug);
		this.source = source;
//		this.producer = producer;
//		this.transactionProducer = transactionProducer;
//		this.setKey(message.getKeys());
//		this.chain = chain;
//		this.messageEntity = messageEntity;
	}

	public boolean isTransactional(){
		return source.isTransactional();
	}
	
	public boolean isDelayMessage() {
		Long startDeliverTime = getMessage().getStartDeliverTime();
		return startDeliverTime!=null && startDeliverTime > 0;
	}
	

}
