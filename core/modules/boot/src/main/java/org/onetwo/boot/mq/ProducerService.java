package org.onetwo.boot.mq;

import java.io.Serializable;

import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;

/**
 * @author wayshall
 * <br/>
 */
//@SuppressWarnings("rawtypes")
public interface ProducerService {

	Object sendMessage(Serializable onsMessage);
	Object sendMessage(Serializable onsMessage, InterceptorPredicate interceptorPredicate);


}