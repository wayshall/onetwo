package org.onetwo.ext.ons.producer;

import org.onetwo.ext.alimq.OnsMessage;

import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;

/**
 * @author wayshall
 * <br/>
 */
public interface TransactionProducerService extends TraceableProducer {

	SendResult sendMessage(OnsMessage onsMessage,
			LocalTransactionExecuter executer, Object arg);

	/***
	 * 伪装一个非事务producer，简化调用
	 * @author wayshall
	 * @return
	 */
	ProducerService fakeProducerService();

}