package org.onetwo.ext.ons.producer;

import org.onetwo.ext.alimq.ExtMessage;
import org.onetwo.ext.rocketmq.transaction.RmqTransactionContext;

/**
 * @author wayshall
 * <br/>
 */
public interface TransactionProducerService extends TraceableProducer {

	/***
	 * 
	 * @param onsMessage
	 * @param executer
	 * @param tranactionListenerArg 应用自定义参数，该参数可以传入本地事务执行器
	 * @return
	 */
	void sendMessage(ExtMessage message, RmqTransactionContext tranactionListenerArg);

}