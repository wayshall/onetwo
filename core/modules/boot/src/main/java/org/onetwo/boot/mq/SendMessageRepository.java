package org.onetwo.boot.mq;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author wayshall
 * <br/>
 */
public interface SendMessageRepository {

	void save(SendMessageContext<?> ctx);

	void updateToSent(SendMessageContext<?> ctx);
	void updateToSent(SendMessageEntity messageEntity);

	void remove(Collection<SendMessageContext<?>> msgCtxs);
	
	
	int lockToBeSendMessage(String locker, Date now);
	List<SendMessageEntity> findLockerMessage(String locker, Date now, int sendCountPerTask);

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