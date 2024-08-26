package org.onetwo.ext.ons.consumer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.boot.mq.exception.ConsumeException;
import org.onetwo.boot.mq.exception.DeserializeMessageException;
import org.onetwo.boot.mq.exception.ImpossibleConsumeException;
import org.onetwo.boot.mq.exception.MQException;
import org.onetwo.common.exception.MessageOnlyServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.alimq.BatchConsumContext;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.JsonMessageSerializer;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;
import org.onetwo.ext.ons.ONSConsumerListenerComposite;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.MessageSerializerType;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class DelegateMessageService implements InitializingBean {
	final Logger logger = ONSUtils.getONSLogger();
	final private MessageDeserializer messageDeserializer;
	private ONSConsumerListenerComposite consumerListenerComposite;
	@Autowired
	private DelegateMessageService delegateMessageService;
	private ONSProperties properties;
	
	
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
	 * 是否检查消费者的参数类型，即被序列化后的数据，是否匹配消费者的消息参数类型，避免不匹配而导致参数类型错误。
	 * @return
	 */
	private boolean isCheckConsumerParameterType() {
		MessageSerializerType stype = this.properties.getSerializer();
		return stype.equals(MessageSerializerType.TYPING_JSON);
	}
	/***
	 * 返回最近一次消费上下文
	 * @author wayshall
	 * @param meta
	 * @param msgs
	 * @param context
	 * @return
	 */
//	@Transactional
	public BatchConsumContext processMessages(ConsumerMeta meta, List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getConsumerAction();

		List<ConsumContext> batchContexts = Lists.newArrayListWithExpectedSize(msgs.size());
		BatchConsumContext batch = new BatchConsumContext(batchContexts);
		
		ConsumContext currentConetxt = null;
		for(MessageExt message : msgs){
			String msgId = ONSUtils.getMessageId(message);
			if (logger.isInfoEnabled()) {
				logger.info("rmq-consumer[{}] received id: {}, key: {}, topic: {}, tag: {}", meta.getConsumerId(), msgId, message.getKeys(), message.getTopic(), message.getTags());
			}
			
			String deserializer = message.getUserProperty(TracableMessage.SERIALIZER_KEY);
			String dataId = message.getUserProperty(TracableMessage.DATA_ID_KEY);
			MessageDeserializer messageDeserializer = getMessageDeserializer(deserializer);
//			Object body = consumer.deserialize(message);
			Object body = message.getBody();
			// 是否自动反序列化
			if(meta.isAutoDeserialize()){
				// 消息原始数据类型
				String sourceClassName = message.getUserProperty(JsonMessageSerializer.PROP_BODY_TYPE);
				// 根据consumer方法（@ONSSubscribe注解的方法）定义的参数，获取需要反序列化的目标类型
				Class<?> targetBodyClass = consumer.getMessageBodyClass(currentConetxt);
				if (targetBodyClass!=null) {
					if (StringUtils.isNotBlank(sourceClassName) && isCheckConsumerParameterType()) {
						Class<?> sourceClass = ReflectUtils.loadClass(sourceClassName);
//					if (!targetBodyClass.getName().equals(sourceClassName)) {
						if (!targetBodyClass.isAssignableFrom(sourceClass)) {
							// 消息的原始类型和目标类型不符
							throw new MQException("The original type(" + sourceClassName + ") of the message "
									+ "does not match the target type(" + targetBodyClass + "), "
									+ "consumer group: " + meta.getConsumerId() + ", "
									+ "consumer bean: " + meta.getConsumerBeanName());
						}
					}
					// 把目标类型设置到property，以供反序列化器使用，注意：如果consumer方法定义的类型和原始类型不兼容，则反序列化的时候会出错
					message.putUserProperty(JsonMessageSerializer.PROP_BODY_TYPE, targetBodyClass.getName());
				}
//				body = messageDeserializer.deserialize(message.getBody(), message);
				body = deserializeMessage(messageDeserializer, message);
				currentConetxt = ConsumContext.builder()
												.messageId(msgId)
												.message(message)
												.deserializedBody(body)
												.messageDeserializer(messageDeserializer)
												.dataId(dataId)
//												.consumerMeta(meta)
												.build();
			}else{
				currentConetxt = ConsumContext.builder()
												.messageId(msgId)
												.message(message)
												.messageDeserializer(messageDeserializer)
//												.deserializedBody(body)
//												.consumerMeta(meta)
												.dataId(dataId)
												.build();
			}
			
			if (meta.isUseBatchMode()) {
				batchContexts.add(currentConetxt);
			} else {
				batch.setCurrentContext(currentConetxt);
				if (meta.shouldWithTransational()) {
					delegateMessageService.consumeMessageWithTransactional(consumer, meta, currentConetxt);
				} else {
					consumeMessage(consumer, meta, currentConetxt);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("rmq-consumer[{}] consumed message. id: {}, topic: {}, tag: {}, body: {}", meta.getConsumerId(), msgId,  message.getTopic(), message.getTags(), currentConetxt.getDeserializedBody());
				} else if(logger.isInfoEnabled()) {
					logger.info("rmq-consumer[{}] consumed message. id: {}, topic: {}, tag: {}", meta.getConsumerId(), msgId,  message.getTopic(), message.getTags());
				}
			}
		}
		
		// 批量消费
		if (meta.isUseBatchMode()) {
			consumeBatchMessages(consumer, meta, batch);
		}
		
		return batch;
	}
	
	private void consumeBatchMessages(CustomONSConsumer consumer, ConsumerMeta meta, BatchConsumContext batch) {
		List<ConsumContext> batchContexts = batch.getContexts();
		for (ConsumContext currentConetxt : batchContexts) {
			consumerListenerComposite.beforeConsumeMessage(meta, currentConetxt);
		}
		
		try {
			consumer.doConsumeBatch(batchContexts);
		} catch (Throwable e) {
			String msg = "rmq-batch-consumer["+meta.getConsumerId()+"] consumed message error. topic: " + meta.getTopic() + ", tags: " + meta.getSubExpression();
			if (batch.getCurrentContext()!=null) {
				msg = buildErrorMessage(meta, batch.getCurrentContext());
			}
			consumerListenerComposite.onBatchConsumeMessageError(batch, e);
			ConsumeException consumeEx = new ConsumeException(msg, e);
			throw consumeEx;
		}
		
		for (ConsumContext currentConetxt : batchContexts) {
			consumerListenerComposite.afterConsumeMessage(meta, currentConetxt);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("rmq-batch-consumer[{}] consumed message. id: {}, topic: {}, tag: {}", meta.getConsumerId(), meta.getTopic(), meta.getSubExpression());
		}
	}
	
	private Object deserializeMessage(MessageDeserializer messageDeserializer, MessageExt message) {
		try {
			Object body = messageDeserializer.deserialize(message.getBody(), message);
			return body;
		} catch (Exception e) {
			String msgId = ONSUtils.getMessageId(message);
			
			// 如果json不能解释，包装成新的异常，不再消费
			JsonMappingException jsonFetal = LangUtils.getCauseException(e, JsonMappingException.class);
			if (jsonFetal!=null) {
				String msg = "deserialize message error, ignore consume. msgId: " + msgId + ", msg: " + e.getMessage();
				throw new ImpossibleConsumeException(msg, e);
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				String msg = "deserialize message error, msgId: " + msgId + ", msg: " + e.getMessage();
				throw new DeserializeMessageException(msg, e);
			}
		}
	}

	private void consumeMessage(CustomONSConsumer consumer, ConsumerMeta meta, ConsumContext currentConetxt) {
		consumerListenerComposite.beforeConsumeMessage(meta, currentConetxt);
		try {
			consumer.doConsume(currentConetxt);
		} catch (MessageOnlyServiceException e) {
			// 此异常无需回滚
			if (logger.isInfoEnabled()) {
				String msg = buildErrorMessage(meta, currentConetxt);
				logger.info("rocketmq consumer will not rollback: {}", msg);
			}
			consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
			// 忽略消费，不再重复消费
			currentConetxt.markWillSkipConsume();
		} catch (Throwable e) {
			String msg = buildErrorMessage(meta, currentConetxt);
//			logger.error(msg, e);
			consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
			ConsumeException consumeEx = new ConsumeException(msg, e);
			throw consumeEx;
		}
		consumerListenerComposite.afterConsumeMessage(meta, currentConetxt);
	}
	
	public static String buildErrorMessage(ConsumerMeta meta, ConsumContext currentConetxt) {
		String msgId = ONSUtils.getMessageId(currentConetxt.getMessage());
		String msg = "rmq-consumer["+meta.getConsumerId()+"] consumed message error. " + 
					"id: " +  msgId + ", key: " + currentConetxt.getMessage().getKeys() +
					", topic: " + currentConetxt.getTopic() + ", tags: " + currentConetxt.getTags();
		return msg;
	}

	@Transactional(noRollbackFor = MessageOnlyServiceException.class)
	public void consumeMessageWithTransactional(CustomONSConsumer consumer, ConsumerMeta meta, ConsumContext currentConetxt) {
		this.consumeMessage(consumer, meta, currentConetxt);
	}

	public void setProperties(ONSProperties properties) {
		this.properties = properties;
	}

}
