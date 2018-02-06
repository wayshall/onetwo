package org.onetwo.ext.ons.transaction;

import java.util.Collection;

import org.onetwo.ext.ons.producer.SendMessageContext;

/**
 * @author wayshall
 * <br/>
 */
public interface SendMessageRepository {

	void save(SendMessageContext ctx);

	void remove(Collection<SendMessageContext> msgCtxs);

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