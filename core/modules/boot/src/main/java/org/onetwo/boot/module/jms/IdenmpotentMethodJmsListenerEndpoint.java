package org.onetwo.boot.module.jms;

import javax.jms.JMSException;
import javax.jms.Session;

import org.onetwo.boot.module.jms.annotation.IdempotentListener;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.lang.Nullable;

/****
 * @see org.springframework.jms.listener.AbstractMessageListenerContainer#doExecuteListener
 * @author way
 *
 */
public class IdenmpotentMethodJmsListenerEndpoint extends MethodJmsListenerEndpoint {

	private DelegateJmsMessageConsumer delegateJmsMessageConsumer;
	private IdempotentListener jmsListener;
	
	public IdenmpotentMethodJmsListenerEndpoint(IdempotentListener jmsListener, DelegateJmsMessageConsumer delegateJmsMessageConsumer) {
		super();
		this.delegateJmsMessageConsumer = delegateJmsMessageConsumer;
		this.jmsListener = jmsListener;
	}

	protected MessagingMessageListenerAdapter createMessageListenerInstance() {
		return new IdenmpotentableMessagingMessageListener();
	}
	
	public class IdenmpotentableMessagingMessageListener extends MessagingMessageListenerAdapter {
	
		/***
		 * @see org.springframework.jms.listener.AbstractMessageListenerContainer#doExecuteListener
		 */
		@Override
		public void onMessage(javax.jms.Message jmsMessage, @Nullable Session session) throws JMSException {
			JmsConsumeContext ctx = JmsConsumeContext.builder()
													.message(jmsMessage)
													.consumer(getBean())
													.listenMethod(getMostSpecificMethod())
													.listener(jmsListener)
													.consumeAction(() -> {
														super.onMessage(jmsMessage, session);
													})
													.build();
			delegateJmsMessageConsumer.processMessage(ctx);
		}
	}
	
	public static interface ConsumerAction {
		public void invokeListener() throws JMSException;
	}
}
