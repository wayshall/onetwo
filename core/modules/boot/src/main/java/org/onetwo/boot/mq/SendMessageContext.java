package org.onetwo.boot.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@AllArgsConstructor
public class SendMessageContext<M> {
	
	final private M message;
//	final private ProducerService<M, R> source;
//	final private SendMessageInterceptorChain<R> chain;
//	private SendMessageEntity messageEntity;
	final private long threadId;
	private boolean debug;

}
