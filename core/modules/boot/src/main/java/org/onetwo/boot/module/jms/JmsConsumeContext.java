package org.onetwo.boot.module.jms;

import java.lang.reflect.Method;

import javax.jms.JMSException;
import javax.jms.Message;

import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.onetwo.boot.module.jms.exception.MQException;
import org.onetwo.boot.mq.IdempotentType;
import org.onetwo.common.exception.BaseException;

import lombok.Data;

@Data
public class JmsConsumeContext {
	
	final Message message;
	final Method listenMethod;
	final Object consumer;
	final IdempotentListener listener;
	final String clientId;
	
	public JmsConsumeContext(String clientId, Message message, Method listenMethod, Object consumer, IdempotentListener listener) {
		super();
		this.message = message;
		this.listenMethod = listenMethod;
		this.listener = listener;
		this.consumer = consumer;
		this.clientId = clientId;
	}
	
	public String getConsumeGroup() {
		return listener.subscription();
	}

	public <T> T getMessageBody(Class<T> bodyType) {
		try {
			return message.getBody(bodyType);
		} catch (JMSException e) {
			throw new MQException("convert to " + bodyType.getSimpleName() + " error: " + e.getMessage(), e);
		}
	}
	
	public String getMessageId() {
		try {
			return message.getJMSMessageID();
		} catch (JMSException e) {
			throw new BaseException("get jms message id error", e);
		}
	}
	
	public IdempotentType getIdempotentType() {
		return listener.idempotentType();
	}
	
	public boolean isDatabaseIdempotent() {
		return IdempotentType.DATABASE == getIdempotentType();
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public String getDestination() {
		return listener.destination();
	}
	
	Object getConsumer() {
		return consumer;
	}
	
}
