package org.onetwo.boot.module.jms;

import java.io.Serializable;
import java.util.List;

import javax.jms.Destination;
import javax.websocket.SendResult;

import org.onetwo.boot.mq.InterceptableMessageSender;
import org.onetwo.boot.mq.ProducerService;
import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.SendMessageInterceptor;
import org.onetwo.boot.mq.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.boot.mq.SendMessageInterceptorChain;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.util.Assert;

/**
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
	public Object send(Serializable message, InterceptorPredicate interceptorPredicate) {
		if(message instanceof JmsMessageCreator){
			return sendMessage((JmsMessageCreator)message);
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
		Assert.notNull(dest);
		return interceptableMessageSender.sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					()->{
						this.jmsMessagingTemplate.convertAndSend(dest, jmsMessage.getJmsMessage());
						return null;
					});
			
			SendMessageContext<Serializable> ctx = SendMessageContext.<Serializable>newBuilder()
															.message(jmsMessage)
															.chain(chain)
															.debug(true)
															.threadId(Thread.currentThread().getId())
															.build();
			chain.setSendMessageContext(ctx);
			chain.setDebug(ctx.isDebug());
			
			return (SendResult)chain.invoke();
		});
	}

	
	

}
