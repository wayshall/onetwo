package org.onetwo.ext.ons.consumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.onetwo.boot.mq.exception.ImpossibleConsumeException;
import org.onetwo.boot.mq.exception.MessageConsumedException;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.BatchConsumContext;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.ConsumeFromWhereProps;
import org.onetwo.ext.ons.ONSUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class ONSPushConsumerStarter implements InitializingBean, DisposableBean {

	private final Logger logger = ONSUtils.getONSLogger(); //LoggerFactory.getLogger(ONSPushConsumerStarter.class);

	@Autowired
	private ApplicationContext applicationContext;
//	private MessageDeserializer messageDeserializer;
	
	private List<MQPushConsumer> consumers = Lists.newArrayList();
	
	private ONSProperties onsProperties;
	
//	private ONSConsumerListenerComposite consumerListenerComposite;
	private DelegateMessageService delegateMessageService;
	@Autowired
	private List<ConsumerProcessor> consumerProcessors;
	

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
//		logger.info("ons consumer init. namesrvAddr: {}", onsProperties.getOnsAddr());
		logger.info("ons consumer init. namesrvAddr: {}", onsProperties.getNamesrvAddr());
//		Assert.notNull(messageDeserializer);
//		Assert.notNull(consumerListenerComposite);
		ConsumerScanner scanner = new ConsumerScanner(applicationContext, this.consumerProcessors);
		Map<String, ConsumerMeta> consumers = scanner.findConsumers();

		BootUtils.asyncInit(()-> {
			consumers.entrySet().forEach(e->{
				try {
					this.initializeConsumers(e.getValue());
				} catch (Exception ex) {
					logger.error("mq consumer initialize error: " + ex.getMessage(), ex);
					if (this.onsProperties.isThrowOnCusomerInitializeError()) {
						throw new BaseException("mq consumer initialize error: " + ex.getMessage(), ex);
					}
				}
			});
		});
		
	}
	
	protected String resloveValue(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}
	

    protected MQPushConsumer createConsumer(final Properties comsumerProperties, ConsumerMeta meta) {
    	DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(meta.getConsumerId());
    	consumer.setMessageModel(meta.getMessageModel());
    	if (meta.getMaxReconsumeTimes()>0){
    		consumer.setMaxReconsumeTimes(meta.getMaxReconsumeTimes());
    	}
    	if (meta.getConsumeTimeoutInMinutes()>0) {
    		consumer.setConsumeTimeout(meta.getConsumeTimeoutInMinutes());
    	}
    	SpringUtils.getMapToBean().injectBeanProperties(comsumerProperties, consumer);
    	consumer.setNamesrvAddr(onsProperties.getNamesrvAddr());
    	return consumer;
    }

	private void initializeConsumers(ConsumerMeta meta) throws InterruptedException, MQClientException {
		Assert.hasText(meta.getConsumerId(), "consumerId can not be empty!");
		Assert.hasText(meta.getTopic(), "topic can not be empty!");
		logger.info("create mq consumerId: {}", meta.getConsumerId());

		Properties comsumerProperties = onsProperties.baseProperties();
		// 配置覆盖注解
		Properties customProps = onsProperties.getConsumers().get(meta.getConsumerId());
		if (customProps!=null){
			comsumerProperties.putAll(customProps);
		}
//		rawConsumer.setMessageModel(meta.getMessageModel());
		
		MQPushConsumer consumer = createConsumer(comsumerProperties, meta);
//		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)ReflectUtils.getFieldValue(consumer, "defaultMQPushConsumer");
		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)consumer;
		rawConsumer.setConsumeFromWhere(meta.getConsumeFromWhere());
		rawConsumer.setConsumeMessageBatchMaxSize(meta.getConsumeMessageBatchMaxSize());
		
		if (StringUtils.isNotBlank(comsumerProperties.getProperty(ConsumerMeta.CONSUME_TIMESTAMP_KEY))) {
			String timestamp = comsumerProperties.getProperty(ConsumerMeta.CONSUME_TIMESTAMP_KEY);
			rawConsumer.setConsumeTimestamp(timestamp);
		} else if (StringUtils.isNotBlank(meta.getConsumeTimestamp())) {
			String timestamp = meta.getConsumeTimestamp();
			rawConsumer.setConsumeTimestamp(timestamp);
		}
		
		meta.setComsumerProperties(comsumerProperties);
		
		configSpecialConsume(rawConsumer);
		
//		consumer.subscribe(meta.getTopic(), meta.getSubExpression(), listener);
		ListenerType listenerType = meta.getListenerType(); 
		if (listenerType==ListenerType.CUSTOM) {
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			registerONSConsumerListener(rawConsumer, meta);
			rawConsumer.start();
		} else {
			rawConsumer.subscribe(meta.getTopic(), meta.getSubExpression());
			rawConsumer.registerMessageListener((MessageListenerConcurrently)meta.getConsumerAction());
			rawConsumer.start();
		}
		logger.info("ONSConsumer[{}] started! meta: {}", meta.getConsumerId(), meta);
	}
	
	private void configSpecialConsume(DefaultMQPushConsumer rawConsumer) {
		ConsumeFromWhereProps specialConsume = this.onsProperties.getSpecialConsume();
		if (!specialConsume.isEnabled()) {
			return ;
		}
		rawConsumer.setConsumeFromWhere(specialConsume.getConsumeFromWhere());
		rawConsumer.setConsumeTimestamp(specialConsume.getConsumeTimestamp());
	}

	private void registerONSConsumerListener(DefaultMQPushConsumer rawConsumer, ConsumerMeta meta) throws MQClientException{
		rawConsumer.registerMessageListener(new MessageListenerConcurrently() {
			
			// 里面的消息是一个集合List而不是单独的msg，这个consumeMessageBatchMaxSize就是控制这个集合的最大大小。
			// 而由于拉取到的一批消息会立刻拆分成N（取决于consumeMessageBatchMaxSize）批消费任务，所以集合中msgs的最大大小是consumeMessageBatchMaxSize和pullBatchSize的较小值。
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

//				ConsumContext currentConetxt = null;
				BatchConsumContext batch = null;
				try {
					batch = delegateMessageService.processMessages(meta, msgs, context);
				} catch(MessageConsumedException e) {
					// 忽略已消费异常
					if (logger.isDebugEnabled()) {
						logger.debug("message has been consumed and will skip: " + e.getMessage(), e);
					} else {
						logger.warn("message has been consumed and will skip: " + e.getMessage());
					}
				} catch(ImpossibleConsumeException e) {
					// 不可能被消费，记录错误并发送提醒
					String errorMsg = "message can not be consumed and will skip: " + e.getMessage();
					logAndMail(errorMsg, e);
//					applicationContext.publishEvent(event);
				} catch (Throwable e) {
					ConsumContext currentConetxt = batch==null?null:batch.getCurrentContext();
					String errorMsg = e.getMessage();
//					if(currentConetxt!=null){
////						consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
//						errorMsg += "currentMessage id: "+currentConetxt.getMessageId()+", topic: "+currentConetxt.getMessage().getTopic()+
//										", tag: "+currentConetxt.getMessage().getTags()+", body: " + currentConetxt.getDeserializedBody();
//					}
					
					// 不可能被消费，记录错误并发送提醒
					if (currentConetxt!=null && currentConetxt.isWillSkipConsume()) {
						logger.warn("rocketmq message will skip. msgKey: {}, error: {}", currentConetxt.getMessage().getKeys(), errorMsg);
//						logAndMail(errorMsg, e);
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}

					logAndMail(errorMsg, e);
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//					throw new BaseException(e);
				}
//				logger.info("consumed firstMessage. id: {}, topic: {}, tag: {}", firstMessage.getMsgId(),  firstMessage.getTopic(), firstMessage.getTags());

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
	}

	private void logAndMail(String errorMsg, Throwable e) {
		logger.error(errorMsg, e);
		try {
			JFishLoggerFactory.findMailLogger().error(errorMsg, e);
		} catch (Exception e2) {
			logger.error("send rocketmq message consume mail error: ", e2.getMessage());
		}
	}

	@Override
	public void destroy() {
		consumers.forEach(c->{
			c.shutdown();
		});
		logger.info("DefaultMQPushConsumer shutdown.");
	}

	public class ConsumerScanner {
//		private ApplicationContext applicationContext;
		private List<ConsumerProcessor> consumerProcessors;
		
		public ConsumerScanner(ApplicationContext applicationContext, List<ConsumerProcessor> consumerProcessors) {
			super();
//			this.applicationContext = applicationContext;
			this.consumerProcessors = consumerProcessors;
		}

		public Map<String, ConsumerMeta> findConsumers() {
			Map<String, ConsumerMeta> consumers = Maps.newHashMap();
			
			consumerProcessors.forEach(processor -> {
				processor.parse(consumers);
			});
			/*for(ListenerType type : ListenerType.values()){
				buildConsumerMeta(consumers, type);
			}
			buildAnnotationConsumers(consumers);*/
			return consumers;
		}
		
	}
}
