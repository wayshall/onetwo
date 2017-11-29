package org.onetwo.ext.rocketmq.consumer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.rocketmq.annotation.RMQConsumer;
import org.onetwo.ext.rocketmq.annotation.RMQSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageConst;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class RocketMQPushConsumerStarter implements InitializingBean, DisposableBean {

	private final Logger logger = LoggerFactory.getLogger(RocketMQPushConsumerStarter.class);

	private String namesrvAddr;
	
	@Autowired
	private ApplicationContext applicationContext;
	private List<DefaultMQPushConsumer> defaultMQPushConsumers = Lists.newArrayList();
	

	public RocketMQPushConsumerStarter() {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("mq consumer init. namesrvAddr: {}", namesrvAddr);
//		Assert.hasText(namesrvAddr, "namesrvAddr can not be empty!");

		Map<String, AppMQConsumer> consumerBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, AppMQConsumer.class);
		List<AppMQConsumer> consumerBeanList = Lists.newArrayList(consumerBeans.values());
		
		ConsumerScanner scanner = new ConsumerScanner(applicationContext);
		consumerBeanList.addAll(scanner.findConsumers());

		Map<ConsumerMeta, List<AppMQConsumer>> consumerGroups = consumerBeanList.stream()
																				.collect(Collectors.groupingBy(c->c.getConsumerMeta()));
		consumerGroups.entrySet().forEach(e->{
			try {
				this.initializeConsumers(e.getKey(), e.getValue());
			} catch (MQClientException | InterruptedException ex) {
				logger.error("mq consumer initialize error: " + ex.getMessage(), ex);
			}
		});
		
	}



	@SuppressWarnings("rawtypes")
	private void initializeConsumers(ConsumerMeta meta, List<AppMQConsumer> consumers) throws InterruptedException, MQClientException {

		Assert.hasText(meta.getGroupName(), "consumerGroup can not be empty!");
		Assert.hasText(meta.getTopic(), "topic can not be empty!");
		logger.info("create mq consumergroup: {}", meta.getGroupName());
		consumers.forEach(c->{
			logger.info("consumer: {}", c.getConsumerMeta());
		});
		DefaultMQPushConsumer defaultMQPushConsumer = this.createAndConfigMQPushConsumer(meta);
		
		defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {

			@SuppressWarnings("unchecked")
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				MessageExt msg = msgs.get(0);
				logger.info("receive id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());
				  
				try {
					consumers.stream().forEach(consumer->{
						ConsumerMeta meta = consumer.getConsumerMeta();
						if(meta.getIgnoreOffSetThreshold()>0){
							long diff = getMessageDiff(msg);
							if(diff>meta.getIgnoreOffSetThreshold()){
								logger.info("message offset diff[{}] is greater than ignoreOffSetThreshold, ignore!", diff);
								return ;
							}
						}
						Object body = consumer.convertMessage(msg);
						logger.info("consume id: {}, body: {}", msg.getMsgId(), body);
						consumer.doConsume(body);
					});
				} catch (Exception e) {
					String errorMsg = "consume message error. id: "+msg.getMsgId()+", topic: "+msg.getTopic()+", tag: "+msg.getTags();
					logger.error(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				logger.info("consume finish. id: {}, topic: {}, tag: {}", msg.getMsgId(),  msg.getTopic(), msg.getTags());

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		defaultMQPushConsumer.start();
		logger.info("defaultMQPushConsumer[{}] start. consumers size: {}", meta.getGroupName(), consumers.size());
		defaultMQPushConsumers.add(defaultMQPushConsumer);
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
	
	protected DefaultMQPushConsumer createAndConfigMQPushConsumer(ConsumerMeta meta) throws InterruptedException, MQClientException {
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(meta.getGroupName());
		defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
		defaultMQPushConsumer.setVipChannelEnabled(false);

		if(meta.getTags()!=null && !meta.getTags().isEmpty()){
			defaultMQPushConsumer.subscribe(meta.getTopic(), StringUtils.join(meta.getTags(), " || "));
		}else{
			defaultMQPushConsumer.subscribe(meta.getTopic(), null);
		}
		
		defaultMQPushConsumer.setConsumeFromWhere(meta.getConsumeFromWhere());
		defaultMQPushConsumer.setMessageModel(meta.getMessageModel());
		
		return defaultMQPushConsumer;
	}

	@Override
	public void destroy() {
		defaultMQPushConsumers.forEach(c->{
			c.shutdown();
		});
		logger.info("DefaultMQPushConsumer shutdown.");
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	@SuppressWarnings("rawtypes")
	public static class ConsumerScanner {
		private ApplicationContext applicationContext;
		
		public ConsumerScanner(ApplicationContext applicationContext) {
			super();
			this.applicationContext = applicationContext;
		}

		public List<AppMQConsumer> findConsumers() {
			Map<String, Object> annotationBeans = applicationContext.getBeansWithAnnotation(RMQConsumer.class);
			return annotationBeans.values().stream().flatMap(bean->{
				return findAppMQConsumer(bean).stream();
			})
			.collect(Collectors.toList());
		}


		private List<AppMQConsumer> findAppMQConsumer(Object bean){
			return findConsumerMethods(bean).stream().map(method->{
				return new DelegateAppMQConsumer(bean, method, AnnotationUtils.findAnnotation(method, RMQSubscribe.class));
			})
			.collect(Collectors.toList());
		}

		private List<Method> findConsumerMethods(Object bean){
			Class<?> targetClass = AopUtils.getTargetClass(bean);
			List<Method> consumerMethods = Lists.newArrayList();
			ReflectionUtils.doWithMethods(targetClass, m->consumerMethods.add(m), m->{
				if(AnnotationUtils.findAnnotation(m, RMQSubscribe.class)!=null){
					if(ArrayUtils.contains(m.getParameterTypes(), MessageExt.class)){
						return true;
					}
					throw new RuntimeException("the parameter type of the consumer method must be a "+MessageExt.class);
				}
				return false;
			});
			return consumerMethods;
		}
		
		class DelegateAppMQConsumer implements AppMQConsumer<MessageExt> {
			private Object target;
			private Method consumerMethod;
			private ConsumerMeta meta;
			public DelegateAppMQConsumer(Object target, Method consumerMethod, RMQSubscribe subscribe) {
				super();
				this.target = target;
				this.consumerMethod = consumerMethod;
				Set<String> tags = Sets.newHashSet(subscribe.tags());
				this.meta = new ConsumerMeta(subscribe.groupName(), subscribe.topic(), tags, 
												subscribe.messageModel(), subscribe.consumeFromWhere(), subscribe.ignoreOffSetThreshold());
			}
			@Override
			public ConsumerMeta getConsumerMeta() {
				return meta;
			}
			public MessageExt convertMessage(MessageExt msg){
				return msg;
			}
			@Override
			public void doConsume(MessageExt msg) {
				ReflectionUtils.invokeMethod(consumerMethod, target, msg);
			}
		}
	}
}
