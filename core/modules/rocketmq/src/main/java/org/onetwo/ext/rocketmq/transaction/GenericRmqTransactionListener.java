package org.onetwo.ext.rocketmq.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/****
 * 通用的事务消息监听器
 * @author way
 *
 */
public class GenericRmqTransactionListener implements TransactionListener {
//	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	private RmqTransactionLogService transactionLogService;
	
	@Override
	@Transactional(propagation = Propagation.MANDATORY) // 如果当前存在事务，就加入当前事务，如果当前不存在事务，就抛出异常
	public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
		this.transactionLogService.save(msg, arg);
		// 总是返回unknow状态即可，让回查决定是否回滚消息
		return LocalTransactionState.UNKNOW;
	}

	/***
	 * 事务回查
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED) // 	如果当前没有事务，就创建一个事务，如果当前存在事务，就加入该事务。
	public LocalTransactionState checkLocalTransaction(MessageExt msg) {
		return transactionLogService.checkLocal(msg);
	}
	
}
