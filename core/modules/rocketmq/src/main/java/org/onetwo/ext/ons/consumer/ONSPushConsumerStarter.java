package org.onetwo.ext.ons.consumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.exception.MessageOnlyServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.alimq.ConsumContext;
import org.onetwo.ext.ons.ListenerType;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.ConsumeFromWhereProps;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.exception.ImpossibleConsumeException;
import org.onetwo.ext.ons.exception.MessageConsumedException;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
		logger.info("ons consumer init. namesrvAddr: {}", onsProperties.getOnsAddr());
//		Assert.notNull(messageDeserializer);
//		Assert.notNull(consumerListenerComposite);

		BootUtils.asyncInit(()-> {
			ConsumerScanner scanner = new ConsumerScanner(applicationContext, this.consumerProcessors);
			Map<String, ConsumerMeta> consumers = scanner.findConsumers();

			consumers.entrySet().forEach(e->{
				try {
					this.initializeConsumers(e.getValue());
				} catch (MQClientException | InterruptedException ex) {
					logger.error("mq consumer initialize error: " + ex.getMessage(), ex);
				}
			});
		});
		
	}
	
	protected String resloveValue(String value){
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
		if (meta.getMaxReconsumeTimes()>0){
			comsumerProperties.setProperty(PropertyKeyConst.MaxReconsumeTimes, String.valueOf(meta.getMaxReconsumeTimes()));
		}
		if (meta.getConsumeTimeoutInMinutes()>0) {
			comsumerProperties.setProperty(PropertyKeyConst.ConsumeTimeout, meta.getConsumeTimeoutInMinutes()+"");
		}
		
		if (meta.getComsumerProperties()!=null) {
			comsumerProperties.putAll(meta.getComsumerProperties());
		}
		// 配置覆盖注解
		Properties customProps = onsProperties.getConsumers().get(meta.getConsumerId());
		if (customProps!=null){
			comsumerProperties.putAll(customProps);
		}
//		rawConsumer.setMessageModel(meta.getMessageModel());
		
		Consumer consumer = createConsumer(comsumerProperties);
		DefaultMQPushConsumer rawConsumer = (DefaultMQPushConsumer)ReflectUtils.getFieldValue(consumer, "defaultMQPushConsumer");
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
				} catch(MessageOnlyServiceException e) {
					// 不可能被消费，记录错误并发送提醒
					String errorMsg = "message can not be consumed and will skip: " + e.getMessage();
//					logger.error(errorMsg, e);
					JFishLoggerFactory.findMailLogger().error(errorMsg, e);
//					applicationContext.publishEvent(event);
				}  catch(ImpossibleConsumeException e) {
					// 不可能被消费，记录错误并发送提醒
					String errorMsg = "message can not be consumed and will skip: " + e.getMessage();
					logger.error(errorMsg, e);
					JFishLoggerFactory.findMailLogger().error(errorMsg, e);
//					applicationContext.publishEvent(event);
				} catch (Exception e) {
					String errorMsg = "consume message error.";
					if(currentConetxt!=null){
//						consumerListenerComposite.onConsumeMessageError(currentConetxt, e);
						errorMsg += "currentMessage id: "+currentConetxt.getMessageId()+", topic: "+currentConetxt.getMessage().getTopic()+
										", tag: "+currentConetxt.getMessage().getTags()+", body: " + currentConetxt.getDeserializedBody();
					}
					logger.error(errorMsg, e);
					JFishLoggerFactory.findMailLogger().error(errorMsg, e);
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
