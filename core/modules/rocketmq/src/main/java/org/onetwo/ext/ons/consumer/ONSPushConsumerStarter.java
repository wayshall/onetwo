package org.onetwo.ext.ons.consumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.consumer.ConsumerMeta.ListenerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ONSPushConsumerStarter implements InitializingBean, DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(ONSPushConsumerStarter.class);

	private String namesrvAddr;
	
	@Autowired
	private ApplicationContext applicationContext;
	private List<Consumer> consumers = Lists.newArrayList();
	
	private ONSProperties onsProperties;
	

	public ONSPushConsumerStarter() {
		super();
	}

	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("ons consumer init. namesrvAddr: {}", namesrvAddr);
//		Assert.hasText(namesrvAddr, "namesrvAddr can not be empty!");

		ConsumerScanner scanner = new ConsumerScanner(applicationContext);
		Map<String, ConsumerMeta> consumers = scanner.findConsumers();

		consumers.entrySet().forEach(e->{
			try {
				this.initializeConsumers(e.getValue());
			} catch (MQClientException | InterruptedException ex) {
				logger.error("mq consumer initialize error: " + ex.getMessage(), ex);
			}
		});
		
	}

	private void initializeConsumers(ConsumerMeta meta) throws InterruptedException, MQClientException {
		Assert.hasText(meta.getConsumerId(), "consumerId can not be empty!");
		Assert.hasText(meta.getTopic(), "topic can not be empty!");
		logger.info("create mq consumerId: {}", meta.getConsumerId());

		Properties comsumerProperties = onsProperties.baseProperties();
		comsumerProperties.setProperty(PropertyKeyConst.ConsumerId, meta.getConsumerId());
		comsumerProperties.setProperty(PropertyKeyConst.MessageModel, meta.getMessageModel().name());
		Properties customProps = onsProperties.getConsumers().get(meta.getConsumerId());
		if(customProps!=null){
			comsumerProperties.putAll(customProps);
		}
//		rawConsumer.setMessageModel(meta.getMessageModel());
		
		Consumer consumer = ONSFactory.createConsumer(comsumerProperties);
		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)ReflectUtils.getFieldValue(consumer, "defaultMQPushConsumer");
		rawConsumer.setConsumeFromWhere(meta.getConsumeFromWhere());
		
//		consumer.subscribe(meta.getTopic(), meta.getSubExpression(), listener);
		ListenerType listenerType = meta.getListenerType(); 
		if(listenerType==ListenerType.CUSTOM){
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			registerONSConsumerListener(rawConsumer, meta);
			rawConsumer.start();
		}else if(listenerType==ListenerType.RMQ){
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			rawConsumer.registerMessageListener((MessageListenerConcurrently)meta.getListener());
			rawConsumer.start();
		}else{
			consumer.subscribe(meta.getTopic(), meta.getSubExpression(), (MessageListener)meta.getListener());
			consumer.start();
		}
		logger.info("ONSConsumer[{}] started!", meta.getConsumerId());
	}
	
	@SuppressWarnings("rawtypes")
	private void registerONSConsumerListener(DefaultMQPushConsumer rawConsumer, ConsumerMeta meta) throws MQClientException{
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getListener();
		rawConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@SuppressWarnings("unchecked")
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				MessageExt currentMessage = null;
				try {
					if(meta.getIgnoreOffSetThreshold()>0){
						long diff = ONSUtils.getMessageDiff(msgs.get(0));
						if(diff>meta.getIgnoreOffSetThreshold()){
							logger.info("message offset diff[{}] is greater than ignoreOffSetThreshold[{}], ignore!", diff, meta.getIgnoreOffSetThreshold());
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						}
					}
					for(MessageExt message : msgs){
						String msgId = message.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
						currentMessage = message;
						logger.info("received id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
						consumer.doConsume(message, consumer.deserialize(message));
						logger.info("consumed message. id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
					}
					
//					List<?> messages = msgs.stream().map(m->consumer.deserialize(m)).collect(Collectors.toList());
//					Map<MessageExt, ?> messages = msgs.stream().collect(Collectors.toMap(m->m, m->consumer.deserialize(m)));
//					consumer.consumMessages(messages, context);
				} catch (Exception e) {
					String errorMsg = "consume message error.";
					if(currentMessage!=null){
						errorMsg += "currentMessage id: "+ONSUtils.getMessageDiff(currentMessage)+", topic: "+currentMessage.getTopic()+", tag: "+currentMessage.getTags();
					}
					logger.error(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
//				logger.info("consumed firstMessage. id: {}, topic: {}, tag: {}", firstMessage.getMsgId(),  firstMessage.getTopic(), firstMessage.getTags());

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

//		rawConsumer.start();
	}

	@Override
	public void destroy() {
		consumers.forEach(c->{
			c.shutdown();
		});
		logger.info("DefaultMQPushConsumer shutdown.");
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	public static class ConsumerScanner {
		private ApplicationContext applicationContext;
		
		public ConsumerScanner(ApplicationContext applicationContext) {
			super();
			this.applicationContext = applicationContext;
		}

		public Map<String, ConsumerMeta> findConsumers() {
			Map<String, ConsumerMeta> consumers = Maps.newHashMap();
			for(ListenerType type : ListenerType.values()){
				buildConsumerMeta(consumers, type);
			}
			return consumers;
		}
		
		private void buildConsumerMeta(Map<String, ConsumerMeta> consumers, ListenerType listenerType){
			Map<String, ?> onsListeners = applicationContext.getBeansOfType(listenerType.getListenerClass());
			onsListeners.forEach((name, bean)->{
				Class<?> targetClass = AopUtils.getTargetClass(bean);
				ONSSubscribe subscribe = AnnotationUtils.findAnnotation(targetClass, ONSSubscribe.class);
				ConsumerMeta meta = ConsumerMeta.builder()
												.consumerId(subscribe.consumerId())
												.topic(subscribe.topic())
												.subExpression(subscribe.subExpression())
												.messageModel(subscribe.messageModel())
												.consumeFromWhere(subscribe.consumeFromWhere())
												.ignoreOffSetThreshold(subscribe.ignoreOffSetThreshold())//ONS 不支持
												.listenerType(listenerType)
												.listener(bean)
												.listenerBeanName(name)
												.build();
				if(consumers.containsKey(meta.getConsumerId())){
					throw new BaseException("duplicate consumerId: " + meta.getConsumerId())
																	.put("add listener", name)
																	.put("exists listener", consumers.get(meta.getConsumerId()).getListenerBeanName());
				}
				consumers.put(meta.getConsumerId(), meta);
			});
		}

		
	}
}
