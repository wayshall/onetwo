package org.onetwo.boot.mq;

import java.io.Serializable;

import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;

/**
 * @author wayshall
 * <br/>
 */
//@SuppressWarnings("rawtypes")
public interface ProducerService<M, R> {

	R sendMessage(M message);
	R sendMessage(M message, InterceptorPredicate interceptorPredicate);
	/***
	 * 无返回值，自动检查见过是否发送成功，否则抛错
	 * @param message
	 * @param interceptorPredicate
	 */
	void send(Serializable message, InterceptorPredicate interceptorPredicate);
	
	/***
	 * 是否事务的producer
	 * @return
	 */
//	default boolean isTransactional() {
//		return false;
//	}

}