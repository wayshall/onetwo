package org.onetwo.ext.rocketmq.transaction;

import java.util.Date;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class RmqTransactionLogService {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private SendMessageRepository sendMessageRepository;
	
	@Transactional(propagation = Propagation.MANDATORY) // 如果当前存在事务，就加入当前事务，如果当前不存在事务，就抛出异常
	public void save(Message msg, Object arg) {
//		RmqTransactionContext context = (RmqTransactionContext) arg;
		SendMessageEntity msgEntity = createSendMessageEntity(msg);
		sendMessageRepository.persist(msgEntity);
		// 总是返回unknow状态即可，让回查决定是否回滚消息
//		return LocalTransactionState.UNKNOW;
	}

	@Transactional(propagation = Propagation.REQUIRED) // 	如果当前没有事务，就创建一个事务，如果当前存在事务，就加入该事务。
	public LocalTransactionState checkLocal(MessageExt msg) {
//		int msgCount = sendMessageRepository.countByMsgKey(msg.getKeys());
//		if (msgCount > 0) {
//			// 说明本地事务成功
//			return LocalTransactionState.COMMIT_MESSAGE;
//		} else {
//			// 未知，可能本地事务未完成
//			return LocalTransactionState.UNKNOW;
//		}
		SendMessageEntity msgEntity = sendMessageRepository.findByMsgKey(msg.getKeys());
		if (msgEntity!=null) {
			// 说明本地事务成功，提交事务消息
			msgEntity.setState(SendStates.SENT);
			sendMessageRepository.update(msgEntity);
			logger.info("the transaction message log has found, commit the message. key: {}", msg.getKeys());
			return LocalTransactionState.COMMIT_MESSAGE;
		} else {
			// 未知，可能本地事务未完成
			return LocalTransactionState.UNKNOW;
		}
	}
	
	protected SendMessageEntity createSendMessageEntity(Message msg){
		SendMessageEntity send = new SendMessageEntity();
		send.setKey(msg.getKeys());
		send.setState(SendStates.HALF);
		send.setBody(messageBodyStoreSerializer.serialize(msg));
		send.setDeliverAt(new Date());
		send.setDelay(false);
		return send;
	}

}
