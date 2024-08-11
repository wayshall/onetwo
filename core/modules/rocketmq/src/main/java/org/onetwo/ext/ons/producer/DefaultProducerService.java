package org.onetwo.ext.ons.producer;

import java.util.Date;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.boot.mq.InterceptableMessageSender;
import org.onetwo.boot.mq.MQUtils;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.onetwo.common.convert.Types;
import org.onetwo.ext.alimq.ExtMessage;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.TracableMessageKey;


/**
 * @author weishao zeng
 * <br/>
 */
public interface DefaultProducerService extends TraceableProducer {

	InterceptableMessageSender<SendResult> getInterceptableMessageSender();
	

	default boolean needSerialize(Object body){
		if(body==null){
			return false;
		}
		return !byte[].class.isInstance(body);
	}

	default void configMessage(ExtMessage message, OnsMessage onsMessage) {
		if(onsMessage instanceof TracableMessage) {
			//自动生成key
			//如果是延迟消息，用实际延迟发送的时间替换已存在的发生时间
			TracableMessage tracableMessage = (TracableMessage) onsMessage;
			if (message.getStartDeliverTime()!=null && message.getStartDeliverTime()>0) {
				tracableMessage.setOccurOn(new Date(message.getStartDeliverTime()));
			}

			if (StringUtils.isNotBlank(tracableMessage.getUserId())) {
				message.putUserProperties(TracableMessage.USER_ID_KEY, tracableMessage.getUserId());
			}
			if (StringUtils.isNotBlank(tracableMessage.getDataId())) {
				message.putUserProperties(TracableMessage.DATA_ID_KEY, tracableMessage.getDataId());
			}
			if (tracableMessage.getOccurOn()==null) {
				tracableMessage.setOccurOn(new Date());
			}
			message.putUserProperties(TracableMessage.OCCUR_ON_KEY, String.valueOf(tracableMessage.getOccurOn().getTime()));
			message.putUserProperties(TracableMessage.SERIALIZER_KEY, tracableMessage.getSerializer());
			message.putUserProperties(TracableMessage.DEBUG_KEY, String.valueOf(tracableMessage.isDebug()));

			TracableMessageKey key = ONSUtils.toKey(message.getTopic(), message.getTag(), tracableMessage);
			if (StringUtils.isBlank(message.getKey())) {
				message.setKey(key.getKey());
			}
			if (StringUtils.isBlank(tracableMessage.getIdentityKey())) {
				tracableMessage.setIdentityKey(key.getIdentityKey());
//				message.putUserProperties(TracableMessage.IDENTITY_KEY, key.getIdentityKey());
			}
			
		}
	}
	
	default public SendResult sendRawMessage(ExtMessage message, final InterceptorPredicate interPredicate, Supplier<Object> actualInvoker){
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		return getInterceptableMessageSender().sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					actualInvoker);
//					()->this.doSendRawMessage(message));
			
			boolean debug = Types.asValue(message.getUserProperties(TracableMessage.DEBUG_KEY), boolean.class, false);
			ONSSendMessageContext ctx = ONSSendMessageContext.builder()
															.message(message)
															.source(this)
//															.producer((ProducerBean)this)
															.chain(chain)
															.debug(debug)
															.threadId(Thread.currentThread().getId())
															.build();
			chain.setSendMessageContext(ctx);
			chain.setDebug(debug);
			
			Object res = chain.invoke();
			if(MQUtils.isSuspendResult(res)){
				return ONSUtils.ONS_SUSPEND;
			}
			return (SendResult)res;
		});
	}
	
}

