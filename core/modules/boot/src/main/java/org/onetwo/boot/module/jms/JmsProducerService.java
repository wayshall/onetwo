package org.onetwo.boot.module.jms;

import java.io.Serializable;
import java.util.List;

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

/**
 * @author wayshall
 * <br/>
 */
public class JmsProducerService implements ProducerService<JmsMessage, Object>, InitializingBean {
	@Autowired(required=false)
	private List<SendMessageInterceptor> sendMessageInterceptors;
	private InterceptableMessageSender<Object> interceptableMessageSender;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.interceptableMessageSender = new InterceptableMessageSender<>(sendMessageInterceptors);		
	}

	@Override
	public Object send(Serializable message, InterceptorPredicate interceptorPredicate) {
		if(message instanceof JmsMessage){
			return sendMessage((JmsMessage)message);
		}
		throw new IllegalArgumentException("error message type:"+message.getClass());
	}

	@Override
	public Object sendMessage(JmsMessage onsMessage) {
		return sendMessage(onsMessage, null);
	}

	@Override
	public Object sendMessage(JmsMessage onsMessage, InterceptorPredicate interPredicate) {
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		return interceptableMessageSender.sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					()->{
						this.jmsMessagingTemplate.convertAndSend(onsMessage.getDestination(), onsMessage.getMessageBody());
						return null;
					});
			
			SendMessageContext<Serializable> ctx = SendMessageContext.<Serializable>newBuilder()
															.message(onsMessage)
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
