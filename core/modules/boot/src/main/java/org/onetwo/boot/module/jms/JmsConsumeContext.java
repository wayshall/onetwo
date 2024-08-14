package org.onetwo.boot.module.jms;

import java.lang.reflect.Method;

import javax.jms.JMSException;
import javax.jms.Message;

import org.onetwo.boot.module.jms.IdenmpotentMethodJmsListenerEndpoint.ConsumerAction;
import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.onetwo.boot.mq.IdempotentType;
import org.onetwo.boot.mq.exception.MQException;
import org.onetwo.common.exception.BaseException;

import lombok.Builder;
import lombok.Data;

@Data
public class JmsConsumeContext {
	
	private final Message message;
	private final Method listenMethod;
	private final Object consumer;
	
	private final IdempotentListener listener;
	
	private final ConsumerAction consumeAction;
	
	@Builder
	public JmsConsumeContext(Message message, Method listenMethod, Object consumer, IdempotentListener listener, ConsumerAction consumeAction) {
		super();
		this.message = message;
		this.listenMethod = listenMethod;
		this.consumer = consumer;
		this.listener = listener;
		this.consumeAction = consumeAction;
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
	
	public boolean isBodyAssignableTo(Class<?> bodyType) {
		try {
			return message.isBodyAssignableTo(JmsMessage.class);
		} catch (JMSException e) {
			return false;
		}
	}
	
	public String getMessageKey() {
		if (isBodyAssignableTo(JmsMessage.class)) {
			JmsMessage<?> msg = getMessageBody(JmsMessage.class);
			if (msg!=null) {
				return msg.getKey();
			}
		}
		try {
			String msgkey = message.getStringProperty(JmsMessage.HEADER_MSG_KEY);
			return msgkey;
		} catch (JMSException e) {
			throw new BaseException("get jms message key error", e);
		}
	}
	
	public IdempotentType getIdempotentType() {
		return listener.idempotentType();
	}
	
	public boolean isDatabaseIdempotent() {
		return IdempotentType.DATABASE == getIdempotentType();
	}
	
	public String getDestination() {
		return listener.destination();
	}
	
	public Object getConsumer() {
		return consumer;
	}
	
	
}
