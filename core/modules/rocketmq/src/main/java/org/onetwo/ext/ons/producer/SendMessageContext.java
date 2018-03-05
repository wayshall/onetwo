package org.onetwo.ext.ons.producer;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.boot.mq.SendMessageEntity;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;

/**
 * @author wayshall
 * <br/>
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SendMessageContext extends org.onetwo.boot.mq.SendMessageContext<Message>{
	
//	final private Message message;
	final private TraceableProducer source;
	final private ProducerBean producer;
	final private TransactionProducerBean transactionProducer;
	final private SendMessageInterceptorChain chain;
	private SendMessageEntity messageEntity;
	/*
	final private long threadId;
	private boolean debug;*/

	@Builder
	public SendMessageContext(Message message,
			SendMessageEntity messageEntity, long threadId,
			boolean debug, TraceableProducer source, ProducerBean producer,
			TransactionProducerBean transactionProducer,
			SendMessageInterceptorChain chain) {
		super(message, threadId, debug);
		this.source = source;
		this.producer = producer;
		this.transactionProducer = transactionProducer;
		this.chain = chain;
		this.messageEntity = messageEntity;
	}
	
	

}
