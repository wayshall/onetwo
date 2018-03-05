package org.onetwo.ext.ons.producer;

import org.onetwo.boot.mq.MQMessage;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;

/**
 * @author wayshall
 * <br/>
 */
public interface TransactionProducerService extends TraceableProducer {

	SendResult sendMessage(MQMessage<Message> onsMessage,
			LocalTransactionExecuter executer, Object arg);

	/***
	 * 伪装一个非事务producer，简化调用
	 * @author wayshall
	 * @return
	 */
	ProducerService fakeProducerService();

}