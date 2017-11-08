package org.onetwo.ext.ons.consumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.ons.ONSProperties;
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
	
	@Autowired
	private ONSProperties onsProperties;
	

	public ONSPushConsumerStarter() {
		super();
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
//		rawConsumer.setMessageModel(meta.getMessageModel());
		
		Consumer consumer = ONSFactory.createConsumer(comsumerProperties);
		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)ReflectUtils.getFieldValue(consumer, "defaultMQPushConsumer");
		rawConsumer.setConsumeFromWhere(meta.getConsumeFromWhere());
		
//		consumer.subscribe(meta.getTopic(), meta.getSubExpression(), listener);
		ListenerType listenerType = meta.getListenerType(); 
		if(listenerType==ListenerType.CUSTOM){
			registerONSConsumerListener(rawConsumer, meta);
		}else if(listenerType==ListenerType.RMQ){
			rawConsumer.registerMessageListener((MessageListenerConcurrently)meta.getListener());
		}else{
			consumer.subscribe(meta.getTopic(), meta.getSubExpression(), (MessageListener)meta.getListener());
		}
	}
	
	private long getMessageDiff(MessageExt msg){
		try {
			long offset = msg.getQueueOffset();//消息自身的offset
			String maxOffset = msg.getProperty(MessageConst.PROPERTY_MAX_OFFSET);//当前最大的消息offset
			long diff = Long.parseLong(maxOffset)-offset;//消费当前消息时积压了多少消息未消费
			return diff;
		} catch (Exception e) {
			return 0;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void registerONSConsumerListener(DefaultMQPushConsumer rawConsumer, ConsumerMeta meta) throws MQClientException{
		final CustomConsumer consumer = (CustomConsumer) meta.getListener();
		rawConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@SuppressWarnings("unchecked")
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				MessageExt msg = msgs.get(0);
				logger.info("receive id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());
				  
				try {
					if(meta.getIgnoreOffSetThreshold()>0){
						long diff = getMessageDiff(msg);
						if(diff>meta.getIgnoreOffSetThreshold()){
							logger.info("message offset diff[{}] is greater than ignoreOffSetThreshold, ignore!", diff);
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						}
					}
					
					List<?> messages = msgs.stream().map(m->consumer.deserialize(m)).collect(Collectors.toList());
					logger.info("consume id: {}, body: {}", msg.getMsgId(), messages);
					consumer.doConsume(messages);
				} catch (Exception e) {
					String errorMsg = "consume message error. id: "+msg.getMsgId()+", topic: "+msg.getTopic()+", tag: "+msg.getTags();
					logger.error(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				logger.info("consume finish. id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		rawConsumer.start();
		logger.info("ONSConsumer[{}] started!");
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
