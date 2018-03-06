package org.onetwo.boot.module.activemq;

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
public class ActivemqProducerService implements ProducerService, InitializingBean {
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
	public Object sendMessage(Serializable onsMessage) {
		return sendMessage(onsMessage, null);
	}

	@Override
	public Object sendMessage(Serializable onsMessage, InterceptorPredicate interPredicate) {
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		return interceptableMessageSender.sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					()->{
						this.jmsMessagingTemplate.convertAndSend(onsMessage);
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
