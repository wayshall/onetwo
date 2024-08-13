package org.onetwo.boot.module.jms;

import java.io.Serializable;
import java.util.List;

import jakarta.jms.Destination;
import jakarta.websocket.SendResult;

import org.onetwo.boot.mq.InterceptableMessageSender;
import org.onetwo.boot.mq.ProducerService;
import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.onetwo.common.utils.Assert;

/**
 * JmsAutoConfiguration
 * 
 * JmxAutoConfiguration
 * 
 * @author wayshall
 * <br/>
 */
public class JmsProducerService implements ProducerService<JmsMessageCreator, Object>, InitializingBean {
	@Autowired(required=false)
	private List<SendMessageInterceptor> sendMessageInterceptors;
	private InterceptableMessageSender<Object> interceptableMessageSender;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private JmsDestinationConverter jmsDestinationConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.interceptableMessageSender = new InterceptableMessageSender<>(sendMessageInterceptors);		
	}

	@Override
	public void send(Serializable message, InterceptorPredicate interceptorPredicate) {
		if(message instanceof JmsMessageCreator){
			sendMessage((JmsMessageCreator)message);
		}
		throw new IllegalArgumentException("error message type:"+message.getClass());
	}

	@Override
	public Object sendMessage(JmsMessageCreator onsMessage) {
		return sendMessage(onsMessage, null);
	}

	@Override
	public Object sendMessage(JmsMessageCreator jmsMessage, InterceptorPredicate interPredicate) {
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		Destination dest = jmsDestinationConverter.getDestination(jmsMessage);
		Assert.notNull(dest, "jms Destination can not be null");
		JmsMessage<? extends Serializable> message = jmsMessage.getJmsMessage();
		
		return interceptableMessageSender.sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					()->{
						this.jmsMessagingTemplate.convertAndSend(dest, message);
						return null;
					});
			
			SendMessageContext<Serializable> ctx = SendMessageContext.<Serializable>newBuilder()
															.message(jmsMessage)
															.chain(chain)
															.debug(false)
															.key(message.getKey())
															.threadId(Thread.currentThread().getId())
															.build();
			chain.setSendMessageContext(ctx);
			chain.setDebug(ctx.isDebug());
			
			return (SendResult)chain.invoke();
		});
	}

	
	

}
