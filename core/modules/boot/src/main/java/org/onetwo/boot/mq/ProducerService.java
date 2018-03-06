package org.onetwo.boot.mq;

import java.io.Serializable;

import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;

/**
 * @author wayshall
 * <br/>
 */
//@SuppressWarnings("rawtypes")
public interface ProducerService<M, R> {

	R sendMessage(M message);
	R sendMessage(M message, InterceptorPredicate interceptorPredicate);
	R send(Serializable message, InterceptorPredicate interceptorPredicate);


}