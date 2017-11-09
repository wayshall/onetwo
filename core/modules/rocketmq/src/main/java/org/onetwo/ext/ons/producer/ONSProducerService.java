package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.SerializationUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.alimq.SendMessageErrorHandler;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProducerListenerComposite;
import org.onetwo.ext.ons.ONSProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ONSProducerService extends ProducerBean implements InitializingBean, DisposableBean {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private SendMessageErrorHandler<SendResult> errorHandler = null;
	private MessageSerializer messageSerializer = msg->SerializationUtils.serialize((Serializable)msg);

	private ONSProperties onsProperties;
	private String producerId;
	
	private ONSProducerListenerComposite producerListenerComposite;
	
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

	@Autowired(required=false)
	public void setMessageSerializer(MessageSerializer messageSerializer) {
		this.messageSerializer = messageSerializer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(producerId);
		Assert.notNull(onsProperties);
		
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

	public void sendMessage(String topic, String tags, Object body){
		Assert.notNull(messageSerializer);
		sendBytesMessage(topic, tags, messageSerializer.serialize(body));
	}
	
	public SendResult sendBytesMessage(String topic, String tags, byte[] body){
		SendResult result =  sendBytesMessage(topic, tags, body, errorHandler);
		return result;
	}
	
	public SendResult sendMessage(SimpleMessage onsMessage){
		return sendMessage(onsMessage, errorHandler);
	}
	
	public SendResult sendMessage(SimpleMessage onsMessage, SendMessageErrorHandler<SendResult> errorHandler){
		Message message = new Message();
		message.setKey(onsMessage.getKey());
		message.setTopic(onsMessage.getTopic());
		message.setTag(onsMessage.getTags());
		message.setBody(this.messageSerializer.serialize(onsMessage.getBody()));
		if(onsMessage.getDelayTimeInMillis()!=null){
			message.setStartDeliverTime(System.currentTimeMillis()+onsMessage.getDelayTimeInMillis());
		}
		return sendRawMessage(message, errorHandler);
	}

	public SendResult sendBytesMessage(String topic, String tags, byte[] body, SendMessageErrorHandler<SendResult> errorHandler){
		Message message = new Message();
		message.setTopic(topic);
		message.setTag(tags);
		message.setBody(body);
		return sendRawMessage(message, errorHandler);
	}
	
	public SendResult sendRawMessage(Message message){
		SendResult result = sendRawMessage(message, errorHandler);
		return result;
	}
	
	public SendResult sendRawMessage(Message message, SendMessageErrorHandler<SendResult> errorHandler){
		try {
			producerListenerComposite.beforeSendMessage(message);
			SendResult sendResult = this.send(message);
			logger.info("send message success. sendResult: {}", sendResult);
			return sendResult;
		} catch (ONSClientException e) {
			producerListenerComposite.onSendMessageError(message, e);
			return this.handleException(e, message, errorHandler).orElseThrow(()->new BaseException("send message error", e));
		}catch (Throwable e) {
			producerListenerComposite.onSendMessageError(message, e);
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
	
	
}
