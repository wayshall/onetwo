package org.onetwo.ext.ons.producer;

import java.util.Optional;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.alimq.MessageSerializer.MessageDelegate;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.ProducerListener.SendMessageContext;
import org.onetwo.ext.alimq.SendMessageErrorHandler;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProducerListenerComposite;
import org.onetwo.ext.ons.ONSProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;

/**
 * @author wayshall
 * <br/>
 */
public class ONSProducerServiceImpl extends ProducerBean implements InitializingBean, DisposableBean, ProducerService {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private SendMessageErrorHandler<SendResult> errorHandler = null;
	private MessageSerializer messageSerializer;

	private ONSProperties onsProperties;
	private String producerId;
	
	private ONSProducerListenerComposite producerListenerComposite;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
	}
	
	@Autowired
	public void setProducerListenerComposite(ONSProducerListenerComposite producerListenerComposite) {
		this.producerListenerComposite = producerListenerComposite;
	}



	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	@Autowired
	public void setMessageSerializer(MessageSerializer messageSerializer) {
		this.messageSerializer = messageSerializer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(producerId);
		Assert.notNull(onsProperties);
		Assert.notNull(messageSerializer);
		
		Properties producerProperties = onsProperties.baseProperties();
		Properties customProps = onsProperties.getProducers().get(producerId);
		if(customProps!=null){
			producerProperties.putAll(customProps);
		}
		producerProperties.setProperty(PropertyKeyConst.ProducerId, producerId);
		
		this.setProperties(producerProperties);
		this.start();
	}
	
	@Override
	public void destroy() throws Exception {
		this.shutdown();
	}
	
	public void setErrorHandler(SendMessageErrorHandler<SendResult> errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public void sendMessage(String topic, String tags, Object body){
		SimpleMessage message = SimpleMessage.builder()
											.topic(topic)
											.tags(tags)
											.body(body)
											.build();
		this.sendMessage(message);
	}
	
	/*public SendResult sendBytesMessage(String topic, String tags, byte[] body){
		SendResult result =  sendBytesMessage(topic, tags, body, errorHandler);
		return result;
	}*/
	
	@Override
	public SendResult sendMessage(OnsMessage onsMessage){
		return sendMessage(onsMessage, errorHandler);
	}
	
	
	@Override
	public SendResult sendMessage(OnsMessage onsMessage, SendMessageErrorHandler<SendResult> errorHandler){
		Message message = onsMessage.toMessage();
		
		String topic = resolvePlaceholders(message.getTopic());
		message.setTopic(topic);
		String tag = resolvePlaceholders(message.getTag());
		message.setTag(tag);
		
		Object body = onsMessage.getBody();
		if(needSerialize(body)){
			message.setBody(this.messageSerializer.serialize(onsMessage.getBody(), new MessageDelegate(message)));
		}else{
			message.setBody((byte[])body);
		}
		
		return sendRawMessage(message, errorHandler);
	}
	

	protected String resolvePlaceholders(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
	}
	
	protected boolean needSerialize(Object body){
		if(body==null){
			return false;
		}
		return !byte[].class.isInstance(body);
	}

	protected SendResult sendRawMessage(Message message, SendMessageErrorHandler<SendResult> errorHandler){
		SendMessageContext ctx = SendMessageContext.builder()
				.producer(this)
				.source(this)
				.message(message)
				.build();
		try {
			producerListenerComposite.beforeSendMessage(ctx);
			SendResult sendResult = this.send(message);
			producerListenerComposite.afterSendMessage(ctx, sendResult);
			return sendResult;
		} catch (ONSClientException e) {
			producerListenerComposite.onSendMessageError(ctx, e);
			return this.handleException(e, message, errorHandler).orElseThrow(()->new BaseException("send message error", e));
		}catch (Throwable e) {
			producerListenerComposite.onSendMessageError(ctx, e);
			return this.handleException(e, message, errorHandler).orElseThrow(()->new BaseException("send message error", e));
		}
	}
	
	protected Optional<SendResult> handleException(Throwable e, Message message){
		return handleException(e, message, this.errorHandler);
	}
	
	protected Optional<SendResult> handleException(Throwable e, Message message, SendMessageErrorHandler<SendResult> errorHandler){
		String errorMsg = "send message error. topic:"+message.getTopic()+", tags:"+message.getTag();
		logger.error(errorMsg);
		if(errorHandler!=null){
			return errorHandler.onError(e);
		}else if(e instanceof ONSClientException){
			throw (ONSClientException)e;
		}else{
			throw new ServiceException("发送消息失败", e);
		}
	}

	@Override
	public <T> T getRawProducer(Class<T> targetClass) {
		return targetClass.cast(this);
	}

	@Override
	public boolean isTransactional() {
		return false;
	}
	
	
}
