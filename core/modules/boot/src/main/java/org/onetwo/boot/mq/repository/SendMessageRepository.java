package org.onetwo.boot.mq.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;

/**
 * @author wayshall
 * <br/>
 */
public interface SendMessageRepository {

	void batchSave(Collection<SendMessageContext<?>> ctxs);
	void save(SendMessageContext<?> ctx);

	void batchUpdateToSent(Collection<SendMessageContext<?>> ctxs);
	void updateToSent(SendMessageContext<?> ctx);
	void updateToSent(SendMessageEntity messageEntity);

	void remove(Collection<SendMessageContext<?>> msgCtxs);
	
	
	int lockSendMessage(String locker, Date now, SendStates sendState);
	List<SendMessageEntity> findLockerMessage(String locker, Date now, SendStates sendState, int sendCountPerTask);

	/***
	 * 查找当前上下文的所有发送消息
	 * 一次过发送当前上下文所有消息时需要
	 * @author wayshall
	 * @return
	 
	Set<SendMessageContext> findAllInCurrentContext();*/

	/***
	 * 清除当前上下文的所有发送消息
	 * @author wayshall
	 
	void clearInCurrentContext();*/

}