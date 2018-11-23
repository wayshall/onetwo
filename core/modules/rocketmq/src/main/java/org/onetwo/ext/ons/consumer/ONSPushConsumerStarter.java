package org.onetwo.ext.ons.consumer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.exception.MessageConsumedException;
import org.slf4j.Logger;
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

	private final Logger logger = ONSUtils.getONSLogger(); //LoggerFactory.getLogger(ONSPushConsumerStarter.class);

	@Autowired
	private ApplicationContext applicationContext;
//	private MessageDeserializer messageDeserializer;
	
	private List<Consumer> consumers = Lists.newArrayList();
	
	private ONSProperties onsProperties;
	
//	private ONSConsumerListenerComposite consumerListenerComposite;
	private DelegateMessageService delegateMessageService;
	

	public ONSPushConsumerStarter(/*MessageDeserializer messageDeserializer*/) {
		super();
//		this.messageDeserializer = messageDeserializer;
	}

	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
	}
	
	/*public void setConsumerListenerComposite(ONSConsumerListenerComposite consumerListenerComposite) {
		this.consumerListenerComposite = consumerListenerComposite;
	}*/

	public void setDelegateMessageService(DelegateMessageService delegateMessageService) {
		this.delegateMessageService = delegateMessageService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("ons consumer init. namesrvAddr: {}", onsProperties.getOnsAddr());
//		Assert.notNull(messageDeserializer);
//		Assert.notNull(consumerListenerComposite);

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
	
	private String resloveValue(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}
	

    protected Consumer createConsumer(final Properties comsumerProperties) {
        return ONSFactory.createConsumer(comsumerProperties);
    }

	private void initializeConsumers(ConsumerMeta meta) throws InterruptedException, MQClientException {
		Assert.hasText(meta.getConsumerId(), "consumerId can not be empty!");
		Assert.hasText(meta.getTopic(), "topic can not be empty!");
		logger.info("create mq consumerId: {}", meta.getConsumerId());

		Properties comsumerProperties = onsProperties.baseProperties();
		comsumerProperties.setProperty(PropertyKeyConst.ConsumerId, meta.getConsumerId());
		comsumerProperties.setProperty(PropertyKeyConst.MessageModel, meta.getMessageModel().name());
		if(meta.getMaxReconsumeTimes()>0){
			comsumerProperties.setProperty(PropertyKeyConst.MaxReconsumeTimes, String.valueOf(meta.getMaxReconsumeTimes()));
		}
		Properties customProps = onsProperties.getConsumers().get(meta.getConsumerId());
		if(customProps!=null){
			comsumerProperties.putAll(customProps);
		}
//		rawConsumer.setMessageModel(meta.getMessageModel());
		
		Consumer consumer = createConsumer(comsumerProperties);
		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)ReflectUtils.getFieldValue(consumer, "defaultMQPushConsumer");
		rawConsumer.setConsumeFromWhere(meta.getConsumeFromWhere());
		meta.setComsumerProperties(comsumerProperties);
		
//		consumer.subscribe(meta.getTopic(), meta.getSubExpression(), listener);
		ListenerType listenerType = meta.getListenerType(); 
		if(listenerType==ListenerType.CUSTOM){
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			registerONSConsumerListener(rawConsumer, meta);
			rawConsumer.start();
		}else if(listenerType==ListenerType.RMQ){
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			rawConsumer.registerMessageListener((MessageListenerConcurrently)meta.getConsumerAction());
			rawConsumer.start();
		}else{
			consumer.subscribe(meta.getTopic(), meta.getSubExpression(), (MessageListener)meta.getConsumerAction());
			consumer.start();
		}
		logger.info("ONSConsumer[{}] started! meta: {}", meta.getConsumerId(), meta);
	}

	private void registerONSConsumerListener(DefaultMQPushConsumer rawConsumer, ConsumerMeta meta) throws MQClientException{
		rawConsumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//				return delegateMessageService.processMessages(meta, msgs, context);
				
				if(meta.getIgnoreOffSetThreshold()>0){
					long diff = ONSUtils.getMessageDiff(msgs.get(0));
					if(diff>meta.getIgnoreOffSetThreshold()){
						logger.warn("message offset diff[{}] is greater than ignoreOffSetThreshold[{}], ignore!", diff, meta.getIgnoreOffSetThreshold());
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
				}

				ConsumContext currentConetxt = null;
				try {
					currentConetxt = delegateMessageService.processMessages(meta, msgs, context);
				} catch(MessageConsumedException e) {
					// 忽略已消费异常
					if (logger.isDebugEnabled()) {
						logger.debug("message has been consumed and will skip: " + e.getMessage(), e);
					} else {
						logger.warn("message has been consumed and will skip: " + e.getMessage());
					}
				} catch (Exception e) {
					String errorMsg = "consume message error.";
					if(currentConetxt!=null){
//						consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
						errorMsg += "currentMessage id: "+currentConetxt.getMessageId()+", topic: "+currentConetxt.getMessage().getTopic()+
										", tag: "+currentConetxt.getMessage().getTags()+", body: " + currentConetxt.getDeserializedBody();
					}
					logger.error(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//					throw new BaseException(e);
				}
//				logger.info("consumed firstMessage. id: {}, topic: {}, tag: {}", firstMessage.getMsgId(),  firstMessage.getTopic(), firstMessage.getTags());

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
	}

	@Override
	public void destroy() {
		consumers.forEach(c->{
			c.shutdown();
		});
		logger.info("DefaultMQPushConsumer shutdown.");
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
																		.put("exists listener", consumers.get(meta.getConsumerId()).getConsumerBeanName());
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
																	.put("exists listener", consumers.get(meta.getConsumerId()).getConsumerBeanName());
				}
				consumers.put(meta.getConsumerId(), meta);
			});
		}

		private ConsumerMeta buildConsumerMeta(ONSSubscribe subscribe, ListenerType listenerType, Object listener, String listernName){
			String subExpression = null;
			if(StringUtils.isBlank(subscribe.subExpression())){
				if(!LangUtils.isEmpty(subscribe.tags())){
					Collection<String> tags = Stream.of(subscribe.tags()).map(tag->resloveValue(tag)).collect(Collectors.toSet());
					subExpression = StringUtils.join(tags, " || ");
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
					.consumerAction(listener)
					.consumerBeanName(listernName)
					.autoDeserialize(subscribe.autoDeserialize())
					.idempotentType(subscribe.idempotent())
					.build();
			return meta;
		}
		
	}
}
