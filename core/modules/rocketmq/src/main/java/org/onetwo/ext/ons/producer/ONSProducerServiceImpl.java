package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.onetwo.boot.mq.InterceptableMessageSender;
import org.onetwo.boot.mq.MQUtils;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.convert.Types;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.ExtMessage;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.alimq.MessageSerializer.MessageDelegate;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.OnsMessage.TracableMessage;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.MessageSerializerType;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.TracableMessageKey;
import org.onetwo.ext.rocketmq.producer.RocketMQProducerService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.onetwo.common.utils.Assert;

/**
 * https://rocketmq.apache.org/zh/docs/4.x/producer/02message1
 * 
 * @author wayshall
 * <br/>
 */
public class ONSProducerServiceImpl extends RocketMQProducerService implements InitializingBean, DisposableBean, DefaultProducerService, RmqProducerService {

	private ONSProperties onsProperties;
//	private String producerId;
//	private ONSProducerListenerComposite producerListenerComposite;
	@Autowired
	private List<SendMessageInterceptor> sendMessageInterceptors;
	private InterceptableMessageSender<SendResult> interceptableMessageSender;
	
	@Autowired
	protected ApplicationContext applicationContext;

	
	public ONSProducerServiceImpl() {
	}
	
	
	@Autowired
	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
	}
	
	public void setProducerId(String producerId) {
//		this.producerId = producerId;
		this.setGroupName(producerId);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(getGroupName(), "groupName must has text");
		Assert.notNull(onsProperties, "onsProperties can not be null");
		Assert.notNull(messageSerializer, "messageSerializer can not be null");
		

		this.interceptableMessageSender = new InterceptableMessageSender<SendResult>(sendMessageInterceptors);
		
		Properties producerProperties = onsProperties.baseProperties();
		Properties customProps = onsProperties.getProducers().get(getGroupName());
		if(customProps!=null){
			producerProperties.putAll(customProps);
		}
//		producerProperties.setProperty(PropertyKeyConst.ProducerId, producerId);
//		producerProperties.setProperty(PropertyKeyConst.GROUP_ID, getGroupName());
		
		this.setNamesrvAddr(onsProperties.getNamesrvAddr());		
		this.setProducerProperties(producerProperties);
		super.afterPropertiesSet();
	}
	
	@Override
	public void destroy() throws Exception {
		super.destroy();
	}
	

	protected boolean needSerialize(Object body){
		if(body==null){
			return false;
		}
		return !byte[].class.isInstance(body);
	}

	protected void configMessage(ExtMessage message, OnsMessage onsMessage) {
		String topic = resolvePlaceholders(message.getTopic());
		checkTopicOrTag(topic);
		message.setTopic(topic);
		
		String tag = resolvePlaceholders(message.getTag());
		message.setTag(tag);
		checkTopicOrTag(tag);
		
		if(onsMessage instanceof TracableMessage) {
			//自动生成key
			//如果是延迟消息，用实际延迟发送的时间替换已存在的发生时间
			TracableMessage tracableMessage = (TracableMessage) onsMessage;
			if (message.getStartDeliverTime()!=null && message.getStartDeliverTime()>0) {
				tracableMessage.setOccurOn(new Date(message.getStartDeliverTime()));
			}

			if (StringUtils.isNotBlank(tracableMessage.getUserId())) {
				message.putUserProperties(TracableMessage.USER_ID_KEY, tracableMessage.getUserId());
			}
			if (StringUtils.isNotBlank(tracableMessage.getDataId())) {
				message.putUserProperties(TracableMessage.DATA_ID_KEY, tracableMessage.getDataId());
			}
			if (tracableMessage.getOccurOn()==null) {
				tracableMessage.setOccurOn(new Date());
			}
			message.putUserProperties(TracableMessage.OCCUR_ON_KEY, String.valueOf(tracableMessage.getOccurOn().getTime()));
			message.putUserProperties(TracableMessage.SERIALIZER_KEY, tracableMessage.getSerializer());
			message.putUserProperties(TracableMessage.DEBUG_KEY, String.valueOf(tracableMessage.isDebug()));

			TracableMessageKey key = ONSUtils.toKey(message.getTopic(), message.getTag(), tracableMessage);
			if (StringUtils.isBlank(message.getKey())) {
				message.setKey(key.getKey());
			}
			if (StringUtils.isBlank(tracableMessage.getIdentityKey())) {
				tracableMessage.setIdentityKey(key.getIdentityKey());
//				message.putUserProperties(TracableMessage.IDENTITY_KEY, key.getIdentityKey());
			}
		}

		Object body = onsMessage.getBody();
		if(needSerialize(body)){
			message.setBody(serializeMessage(onsMessage));
		}else{
			message.setBody((byte[])body);
		}
	}
	
	protected SendResult sendRawMessage(ExtMessage message, final InterceptorPredicate interPredicate, Supplier<Object> actualInvoker){
		return sendRawMessage(message, interPredicate, actualInvoker, null);
	}
	
	protected SendResult sendRawMessage(ExtMessage message, final InterceptorPredicate interPredicate, Supplier<Object> actualInvoker, SendMessageContextConfigurer configurer){
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		return getInterceptableMessageSender().sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					actualInvoker);
//					()->this.doSendRawMessage(message));
			
			boolean debug = Types.asValue(message.getUserProperties(TracableMessage.DEBUG_KEY), boolean.class, false);
			ONSSendMessageContext ctx = ONSSendMessageContext.builder()
															.message(message)
															.source(this)
//															.producer((ProducerBean)this)
															.chain(chain)
															.debug(debug)
															.threadId(Thread.currentThread().getId())
															.build();
			
			if (configurer!=null) {
				configurer.apply(ctx);
			}
			chain.setSendMessageContext(ctx);
			chain.setDebug(debug);
			
			Object res = chain.invoke();
			if(MQUtils.isSuspendResult(res)){
				return ONSUtils.ONS_SUSPEND;
			}
			return (SendResult)res;
		});
	}
	
	@Override
	public void sendMessage(String topic, String tags, Object body){
		SimpleMessage message = SimpleMessage.builder()
											.topic(topic)
											.tags(tags)
											.body(body)
											.build();
		SendResult result = this.sendMessage(message);
		checkSendResult(result);
	}
	
	/*public SendResult sendBytesMessage(String topic, String tags, byte[] body){
		SendResult result =  sendBytesMessage(topic, tags, body, errorHandler);
		return result;
	}*/
	
	
	@Override
	public SendResult sendMessage(OnsMessage onsMessage){
		return sendMessage(onsMessage, SendMessageFlags.Default);
	}
	

//	public void send(OnsMessage onsMessage){
//		SendResult result = sendMessage(onsMessage, SendMessageFlags.Default);
//		checkSendResult(result);
//	}
	

	@Override
	public void send(Serializable onsMessage, InterceptorPredicate interceptorPredicate) {
		SendResult result; 
		if(onsMessage instanceof ExtMessage){
			result = sendRawMessage((ExtMessage)onsMessage, interceptorPredicate);
		}else if(onsMessage instanceof OnsMessage){
			result = sendMessage((OnsMessage)onsMessage, interceptorPredicate);
		}else{
			throw new IllegalArgumentException("error message type: " + onsMessage.getClass());
		}
		checkSendResult(result);
	}

	@Override
	public SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate){
		Object body = onsMessage.getBody();
		if(body instanceof ExtMessage){
			return sendRawMessage((ExtMessage)body, interceptorPredicate);
		}
		
		ExtMessage message = onsMessage.toMessage();
		configMessage(message, onsMessage);
		
		return sendRawMessage(message, interceptorPredicate);
	}
	
	protected byte[] serializeMessage(Object body) {
		if (body instanceof OnsMessage) {
			OnsMessage onsMessage = (OnsMessage)body;
			MessageSerializer messageSerializer = getMessageSerializer(onsMessage);
			byte[] data = messageSerializer.serialize(onsMessage.getBody(), new MessageDelegate(onsMessage.toMessage()));
			return data;
		} else {
			return super.serializeMessage(body);
		}
	}
	
	protected void checkTopicOrTag(String name) {
		if (StringUtils.isBlank(name) || name.contains(" ") || name.contains(FileUtils.UNICODE_ZERO_WIDTH_SPACE)) {
			throw new BaseException("invalid topic or tag: [" + name + "]");
		}
	}

	private MessageSerializer getMessageSerializer(OnsMessage onsMessage) {
		if(!TracableMessage.class.isInstance(onsMessage)) {
			return this.messageSerializer;
		}
		TracableMessage tracableMessage = (TracableMessage) onsMessage;
		MessageSerializer messageSerializer = this.messageSerializer;
		if (StringUtils.isNotBlank(tracableMessage.getSerializer())) {
			messageSerializer = MessageSerializerType.valueOf(tracableMessage.getSerializer().toUpperCase()).getSerializer();
		}
		return messageSerializer;
	}
	/*private void configMessage(Message message, OnsMessage onsMessage) {
		if(onsMessage instanceof TracableMessage) {
			//自动生成key
			//如果是延迟消息，用实际延迟发送的时间替换已存在的发生时间
			TracableMessage tracableMessage = (TracableMessage) onsMessage;
			if (message.getStartDeliverTime()>0) {
				tracableMessage.setOccurOn(new Date(message.getStartDeliverTime()));
			}

			if (StringUtils.isNotBlank(tracableMessage.getUserId())) {
				message.putUserProperties(TracableMessage.USER_ID_KEY, tracableMessage.getUserId());
			}
			if (StringUtils.isNotBlank(tracableMessage.getDataId())) {
				message.putUserProperties(TracableMessage.DATA_ID_KEY, tracableMessage.getDataId());
			}
			if (tracableMessage.getOccurOn()==null) {
				tracableMessage.setOccurOn(new Date());
			}
			message.putUserProperties(TracableMessage.OCCUR_ON_KEY, String.valueOf(tracableMessage.getOccurOn().getTime()));

			TracableMessageKey key = ONSUtils.toKey(message.getTopic(), message.getTag(), tracableMessage);
			if (StringUtils.isBlank(message.getKey())) {
				message.setKey(key.getKey());
			}
			if (StringUtils.isBlank(tracableMessage.getIdentityKey())) {
				tracableMessage.setIdentityKey(key.getIdentityKey());
//				message.putUserProperties(TracableMessage.IDENTITY_KEY, key.getIdentityKey());
			}
		}
	}*/
	
	protected String resolvePlaceholders(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}
	
/*	protected boolean needSerialize(Object body){
		if(body==null){
			return false;
		}
		return !byte[].class.isInstance(body);
	}
*/	
	public SendResult sendRawMessage(ExtMessage message, final InterceptorPredicate interPredicate) {
		return this.sendRawMessage(message, interPredicate, ()->this.doSendRawMessage(message));
	}
	
	/*protected SendResult sendRawMessage(Message message, final InterceptorPredicate interPredicate){
		final InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		return interceptableMessageSender.sendIntercetableMessage(interPredicate, messageInterceptors->{
			SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
					interceptorPredicate,
					()->this.doSendRawMessage(message));
			
			ONSSendMessageContext ctx = ONSSendMessageContext.builder()
															.message(message)
															.source(this)
//															.producer(this)
															.chain(chain)
															.debug(true)
															.threadId(Thread.currentThread().getId())
															.build();
			chain.setSendMessageContext(ctx);
			chain.setDebug(ctx.isDebug());
			
			Object res = chain.invoke();
			if(MQUtils.isSuspendResult(res)){
				return ONSUtils.ONS_SUSPEND;
			}
			return (SendResult)res;
		});
	}*/
	

	public SendResult doSendRawMessage(ExtMessage message){
		try {
			if (logger.isInfoEnabled()) {
				logger.info("do send raw message: {}", message.getKey());
			}
//			return this.send(message);
			return this.defaultMQProducer.send(message);
		} catch (MQClientException e) {
			handleException(e, message);
		}catch (Throwable e) {
			handleException(e, message);
		}
		return null;
	}
	
	public <T> T getRawProducer(Class<T> targetClass) {
		return targetClass.cast(this.defaultMQProducer);
	}

//	@Override
	public boolean isTransactional() {
		return false;
	}

	@Override
	public InterceptableMessageSender<SendResult> getInterceptableMessageSender() {
		return interceptableMessageSender;
	}
	
	protected void checkSendResult(SendResult result) {
		ONSUtils.checkSendResult(result);
	}
	
}
