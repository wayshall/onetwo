package org.onetwo.ext.ons.consumer;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.boot.mq.exception.MQException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.SpringMergedAnnotationFinder;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.annotation.ONSConsumer;
import org.onetwo.ext.ons.annotation.ONSSubscribe;
import org.onetwo.ext.ons.annotation.ONSSubscribe.ConsumerProperty;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class ONSSubscribeProcessor implements ConsumerProcessor {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ONSProperties onsProperties;
	

	@Override
	public void parse(Map<String, ConsumerMeta> consumers) {
		for(ListenerType type : ListenerType.values()){
			// 根据接口类扫描消费者
			buildConsumerMeta(consumers, type);
		}
		// 扫描所有用@ONSConsumer标记了bean，作为消费者
		buildAnnotationConsumers(consumers);
	}
	
	/****
	 * 扫描所有用@ONSConsumer标记了bean，作为消费者
	 * @param consumers
	 */
	public void buildAnnotationConsumers(Map<String, ConsumerMeta> consumers) {
		Map<String, ?> onsListeners = applicationContext.getBeansWithAnnotation(ONSConsumer.class);
		onsListeners.forEach((name, bean)->{
			//one bean multip methods
			ConsumerProcessor.findConsumerBeanMethods(bean, ONSSubscribe.class).forEach(method->{
				ONSSubscribe subscribe = SpringMergedAnnotationFinder.INSTANCE.getAnnotation(method, ONSSubscribe.class);
				DelegateCustomONSConsumer delegate = new DelegateCustomONSConsumer(bean, method);
//				ConsumerMeta meta = buildConsumerMeta(subscribe, ListenerType.CUSTOM, delegate, name);
				ConsumerMeta meta;
				try {
					meta = buildConsumerMeta(subscribe, ListenerType.CUSTOM, delegate, name);
				} catch (Exception e) {
					throw new MQException("build consumer["+name+","+method.getDeclaringClass().getName()+"] error: "+e.getMessage(), e);
				}
				
				if(consumers.containsKey(meta.getConsumerId())){
					throw new MQException("duplicate consumerId: " + meta.getConsumerId())
																	.put("add listener", name)
																	.put("exists listener", consumers.get(meta.getConsumerId()).getConsumerBeanName());
				}
				consumers.put(meta.getConsumerId(), meta);
			});
		});
	}


	private void buildConsumerMeta(Map<String, ConsumerMeta> consumers, ListenerType listenerType){
		Map<String, ?> onsListeners = applicationContext.getBeansOfType(listenerType.getListenerClass());
		onsListeners.forEach((name, bean)->{
			Class<?> targetClass = AopUtils.getTargetClass(bean);
			ONSSubscribe subscribe = AnnotationUtils.findAnnotation(targetClass, ONSSubscribe.class);
			
			ConsumerMeta meta;
			try {
				meta = buildConsumerMeta(subscribe, listenerType, bean, name);
			} catch (Exception e) {
				throw new MQException("build consumer["+name+","+targetClass.getName()+"] error: "+e.getMessage(), e);
			}
			
			if(consumers.containsKey(meta.getConsumerId())){
				throw new MQException("duplicate consumerId: " + meta.getConsumerId())
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
		if (StringUtils.isBlank(consumerId) && listener instanceof DelegateCustomONSConsumer) {
			DelegateCustomONSConsumer d = (DelegateCustomONSConsumer) listener;
			consumerId = AopUtils.getTargetClass(d.getTarget()).getSimpleName() + "-" + d.getConsumerMethod().getName();
		}
		String consumerPrefix = onsProperties.getConsumerPrefix();
		if (StringUtils.isNotBlank(consumerPrefix) && 
				!consumerId.startsWith(consumerPrefix) && 
				subscribe.appendConsumerPrefix()) {
			consumerId = consumerPrefix + "-" +consumerId;
		}
		
		ConsumerProperty[] onsProperties = subscribe.properties();
		Map<String, String> props = Stream.of(onsProperties).collect(Collectors.toMap(prop->prop.name(), prop->resloveValue(prop.value())));
		Properties properties = new Properties();
		properties.putAll(props);
		
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
				.consumeTimestamp(subscribe.consumeTimestamp())
				.consumeTimeoutInMinutes(subscribe.consumeTimeoutInMinutes())
				.autoDeserialize(subscribe.autoDeserialize())
				.idempotentType(subscribe.idempotent())
				.appendConsumerPrefix(subscribe.appendConsumerPrefix())
				.consumeMessageBatchMaxSize(subscribe.consumeMessageBatchMaxSize())
				.comsumerProperties(properties)
				.useBatchMode(subscribe.useBatchMode())
				.build();
		return meta;
	}

	private String resloveValue(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}
}

