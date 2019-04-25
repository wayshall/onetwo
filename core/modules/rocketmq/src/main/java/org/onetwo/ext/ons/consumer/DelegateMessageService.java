package org.onetwo.ext.ons.consumer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.JsonMessageSerializer;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;
import org.onetwo.ext.ons.ONSConsumerListenerComposite;
import org.onetwo.ext.ons.ONSProperties.MessageSerializerType;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.exception.ConsumeException;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DelegateMessageService implements InitializingBean {
	final Logger logger = ONSUtils.getONSLogger();
	final private MessageDeserializer messageDeserializer;
	private ONSConsumerListenerComposite consumerListenerComposite;
	
	
	public DelegateMessageService(MessageDeserializer messageDeserializer, ONSConsumerListenerComposite consumerListenerComposite) {
		super();
		this.messageDeserializer = messageDeserializer;
		this.consumerListenerComposite = consumerListenerComposite;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(messageDeserializer, "messageDeserializer can not be null");
		Assert.notNull(consumerListenerComposite, "consumerListenerComposite can not be null");
	}

	private MessageDeserializer getMessageDeserializer(String deserializer) {
		MessageDeserializer messageDeserializer = this.messageDeserializer;
		if (StringUtils.isNotBlank(deserializer)) {
			messageDeserializer = MessageSerializerType.valueOf(deserializer.toUpperCase()).getDeserializer();
		}
		return messageDeserializer;
	}
	/***
	 * 返回最近一次消费上下文
	 * @author wayshall
	 * @param meta
	 * @param msgs
	 * @param context
	 * @return
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public ConsumContext processMessages(ConsumerMeta meta, List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getConsumerAction();

		ConsumContext currentConetxt = null;
		for(MessageExt message : msgs){
			String msgId = ONSUtils.getMessageId(message);
			if (logger.isInfoEnabled()) {
				logger.info("rmq-consumer[{}] received id: {}, key: {}, topic: {}, tag: {}", meta.getConsumerId(), msgId, message.getKeys(), message.getTopic(), message.getTags());
			}
			
			String deserializer = message.getUserProperty(TracableMessage.SERIALIZER_KEY);
			MessageDeserializer messageDeserializer = getMessageDeserializer(deserializer);
//			Object body = consumer.deserialize(message);
			Object body = message.getBody();
			if(meta.isAutoDeserialize()){
				Class<?> bodyClass = consumer.getMessageBodyClass(currentConetxt);
				if (bodyClass!=null) {
					message.putUserProperty(JsonMessageSerializer.PROP_BODY_TYPE, bodyClass.getName());
				}
				body = messageDeserializer.deserialize(message.getBody(), message);
				currentConetxt = ConsumContext.builder()
												.messageId(msgId)
												.message(message)
												.deserializedBody(body)
												.messageDeserializer(messageDeserializer)
//												.consumerMeta(meta)
												.build();
			}else{
				currentConetxt = ConsumContext.builder()
												.messageId(msgId)
												.message(message)
												.messageDeserializer(messageDeserializer)
//												.deserializedBody(body)
//												.consumerMeta(meta)
												.build();
			}
			
			consumeMessage(consumer, meta, currentConetxt);
			if (logger.isDebugEnabled()) {
				logger.debug("rmq-consumer[{}] consumed message. id: {}, topic: {}, tag: {}, body: {}", meta.getConsumerId(), msgId,  message.getTopic(), message.getTags(), currentConetxt.getDeserializedBody());
			} else if(logger.isInfoEnabled()) {
				logger.info("rmq-consumer[{}] consumed message. id: {}, topic: {}, tag: {}", meta.getConsumerId(), msgId,  message.getTopic(), message.getTags());
			}
		}
		return currentConetxt;
	}

	@SuppressWarnings("rawtypes")
	private void consumeMessage(CustomONSConsumer consumer, ConsumerMeta meta, ConsumContext currentConetxt) {
		consumerListenerComposite.beforeConsumeMessage(meta, currentConetxt);
		try {
			consumer.doConsume(currentConetxt);
		} catch (Exception e) {
			String msgId = ONSUtils.getMessageId(currentConetxt.getMessage());
			String msg = "rmq-consumer["+meta.getConsumerId()+"] consumed message error. " + 
						"id: " +  msgId + ", key: " + currentConetxt.getMessage().getKeys();
			logger.error(msg, e);
			consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
			throw new ConsumeException(msg, e);
		}
		consumerListenerComposite.afterConsumeMessage(meta, currentConetxt);
	}

}
