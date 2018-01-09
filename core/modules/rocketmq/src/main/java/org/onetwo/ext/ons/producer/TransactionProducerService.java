package org.onetwo.ext.ons.producer;

import org.onetwo.ext.alimq.SendMessageErrorHandler;
import org.onetwo.ext.alimq.SimpleMessage;

import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;

/**
 * @author wayshall
 * <br/>
 */
public interface TransactionProducerService {

	SendResult sendMessage(SimpleMessage onsMessage,
			LocalTransactionExecuter executer, Object arg);

	SendResult sendMessage(SimpleMessage onsMessage,
			LocalTransactionExecuter executer, Object arg,
			SendMessageErrorHandler<SendResult> errorHandler);
	
	/***
	 * 伪装一个非事务producer，简化调用
	 * @author wayshall
	 * @return
	 */
	ProducerService fakeProducerService();

}