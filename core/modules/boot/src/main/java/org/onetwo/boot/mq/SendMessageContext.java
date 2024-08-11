package org.onetwo.boot.mq;

import java.io.Serializable;

import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class SendMessageContext<M extends Serializable> {
	
	final private M message;
//	final private ProducerService<M, R> source;
	final private SendMessageInterceptorChain chain;
	private SendMessageEntity messageEntity;
	final private long threadId;
	private boolean debug;
	final private String key;
	

	@Builder(builderMethodName="newBuilder")
	public SendMessageContext(String key, M message, SendMessageInterceptorChain chain,
			SendMessageEntity messageEntity, long threadId, boolean debug) {
		super();
		this.message = message;
		this.chain = chain;
		this.messageEntity = messageEntity;
		this.threadId = threadId;
		this.debug = debug;
		this.key = key;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public M getMessage(){
		return message;
	}

	public boolean isTransactional(){
		return false;
	}
	
	public boolean isDelayMessage() {
		return false;
	}
}
