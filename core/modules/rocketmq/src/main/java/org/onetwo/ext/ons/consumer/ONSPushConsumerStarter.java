package org.onetwo.ext.ons.consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.alimq.MessageDeserializer;
import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.ONSConsumerListenerComposite;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class ONSPushConsumerStarter implements InitializingBean, DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(ONSPushConsumerStarter.class);

	private String namesrvAddr;
	
	@Autowired
	private ApplicationContext applicationContext;
	private MessageDeserializer messageDeserializer;
	
	private List<Consumer> consumers = Lists.newArrayList();
	
	private ONSProperties onsProperties;
	
	private ONSConsumerListenerComposite consumerListenerComposite;
	

	public ONSPushConsumerStarter(MessageDeserializer messageDeserializer) {
		super();
		this.messageDeserializer = messageDeserializer;
	}

	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
	}
	
	public void setConsumerListenerComposite(ONSConsumerListenerComposite consumerListenerComposite) {
		this.consumerListenerComposite = consumerListenerComposite;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("ons consumer init. namesrvAddr: {}", namesrvAddr);
		Assert.notNull(messageDeserializer);
		Assert.notNull(consumerListenerComposite);

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
	
	private String resloveValue(String value	){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}

	private void initializeConsumers(ConsumerMeta meta) throws InterruptedException, MQClientException {
		Assert.hasText(meta.getConsumerId(), "consumerId can not be empty!");
		Assert.hasText(meta.getTopic(), "topic can not be empty!");
		logger.info("create mq consumerId: {}", meta.getConsumerId());

		Properties comsumerProperties = onsProperties.baseProperties();
		comsumerProperties.setProperty(PropertyKeyConst.ConsumerId, meta.getConsumerId());
		comsumerProperties.setProperty(PropertyKeyConst.MessageModel, meta.getMessageModel().name());
		comsumerProperties.setProperty(PropertyKeyConst.MaxReconsumeTimes, String.valueOf(meta.getMaxReconsumeTimes()));
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
		logger.info("ONSConsumer[{}] started! meta: {}", meta.getConsumerId(), meta);
	}
	
	@SuppressWarnings("rawtypes")
	private void registerONSConsumerListener(DefaultMQPushConsumer rawConsumer, ConsumerMeta meta) throws MQClientException{
		final CustomONSConsumer consumer = (CustomONSConsumer) meta.getListener();
		rawConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				ConsumContext currentConetxt = null;
				try {
					if(meta.getIgnoreOffSetThreshold()>0){
						long diff = ONSUtils.getMessageDiff(msgs.get(0));
						if(diff>meta.getIgnoreOffSetThreshold()){
							logger.info("message offset diff[{}] is greater than ignoreOffSetThreshold[{}], ignore!", diff, meta.getIgnoreOffSetThreshold());
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						}
					}
					for(MessageExt message : msgs){
						String msgId = ONSUtils.getMessageId(message);
						logger.info("received id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
						
//						Object body = consumer.deserialize(message);
						Object body = messageDeserializer.deserialize(message.getBody());
						currentConetxt = ConsumContext.builder()
														.messageId(msgId)
														.message(message)
														.deserializedBody(body)
														.build();
						
						consumerListenerComposite.beforeConsumeMessage(currentConetxt);
						consumer.doConsume(currentConetxt);
						consumerListenerComposite.afterConsumeMessage(currentConetxt);
						logger.info("consumed message. id: {}, topic: {}, tag: {}", msgId,  message.getTopic(), message.getTags());
					}
				} catch (Exception e) {
					String errorMsg = "consume message error.";
					if(currentConetxt!=null){
						consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
						errorMsg += "currentMessage id: "+currentConetxt.getMessageId()+", topic: "+currentConetxt.getMessage().getTopic()+", tag: "+currentConetxt.getMessage().getTags();
					}
					logger.error(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//					throw new BaseException(e);
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

	public class ConsumerScanner {
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
			buildAnnotationConsumers(consumers);
			return consumers;
		}
		
		public void buildAnnotationConsumers(Map<String, ConsumerMeta> consumers) {
			Map<String, ?> onsListeners = applicationContext.getBeansWithAnnotation(ONSConsumer.class);
			onsListeners.forEach((name, bean)->{
				//one bean multip methods
				findConsumerMethods(bean).forEach(method->{
					ONSSubscribe subscribe = AnnotationUtils.findAnnotation(method, ONSSubscribe.class);
					DelegateCustomONSConsumer delegate = new DelegateCustomONSConsumer(bean, method);
					ConsumerMeta meta = buildConsumerMeta(subscribe, ListenerType.CUSTOM, delegate, name);
					if(consumers.containsKey(meta.getConsumerId())){
						throw new BaseException("duplicate consumerId: " + meta.getConsumerId())
																		.put("add listener", name)
																		.put("exists listener", consumers.get(meta.getConsumerId()).getListenerName());
					}
					consumers.put(meta.getConsumerId(), meta);
				});
			});
		}
		
		private List<Method> findConsumerMethods(Object bean){
			Class<?> targetClass = AopUtils.getTargetClass(bean);
			List<Method> consumerMethods = Lists.newArrayList();
			ReflectionUtils.doWithMethods(targetClass, m->consumerMethods.add(m), m->{
				if(AnnotationUtils.findAnnotation(m, ONSSubscribe.class)!=null){
					Parameter[] parameters = m.getParameters();
					if(parameters.length==0 || parameters.length>2){
						throw new BaseException("the maximum parameter of consumer method is two.");
					}
					if(parameters[0].getType()!=ConsumContext.class){
						throw new BaseException("the first parameter type of the consumer method must be: "+ConsumContext.class);
					}
					return true;
				}
				return false;
			});
			return consumerMethods;
		}
		
		private void buildConsumerMeta(Map<String, ConsumerMeta> consumers, ListenerType listenerType){
			Map<String, ?> onsListeners = applicationContext.getBeansOfType(listenerType.getListenerClass());
			onsListeners.forEach((name, bean)->{
				Class<?> targetClass = AopUtils.getTargetClass(bean);
				ONSSubscribe subscribe = AnnotationUtils.findAnnotation(targetClass, ONSSubscribe.class);
				ConsumerMeta meta = buildConsumerMeta(subscribe, listenerType, bean, name);
				
				if(consumers.containsKey(meta.getConsumerId())){
					throw new BaseException("duplicate consumerId: " + meta.getConsumerId())
																	.put("add listener", name)
																	.put("exists listener", consumers.get(meta.getConsumerId()).getListenerName());
				}
				consumers.put(meta.getConsumerId(), meta);
			});
		}

		private ConsumerMeta buildConsumerMeta(ONSSubscribe subscribe, ListenerType listenerType, Object listener, String listernName){
			String subExpression = null;
			if(StringUtils.isBlank(subscribe.subExpression())){
				if(!LangUtils.isEmpty(subscribe.tags())){
					subExpression = StringUtils.join(subscribe.tags(), " || ");
				}
			}else{
				subExpression = subscribe.subExpression();
			}
			String topic = resloveValue(subscribe.topic());
			String consumerId = resloveValue(subscribe.consumerId());
			ConsumerMeta meta = ConsumerMeta.builder()
					.consumerId(consumerId)
					.topic(topic)
					.subExpression(subExpression)
					.messageModel(subscribe.messageModel())
					.consumeFromWhere(subscribe.consumeFromWhere())
					.maxReconsumeTimes(subscribe.maxReconsumeTimes())
					.ignoreOffSetThreshold(subscribe.ignoreOffSetThreshold())//ONS类型的listener不支持
					.listenerType(listenerType)
					.listener(listener)
					.listenerName(listernName)
					.build();
			return meta;
		}
		
	}
}
