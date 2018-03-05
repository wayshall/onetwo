package org.onetwo.boot.mq;

import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;

/**
 * @author wayshall
 * <br/>
 */
//@SuppressWarnings("rawtypes")
public interface ProducerService<M, R> {

	R sendMessage(MQMessage<M> onsMessage);
	R sendMessage(MQMessage<M> onsMessage, InterceptorPredicate interceptorPredicate);


}