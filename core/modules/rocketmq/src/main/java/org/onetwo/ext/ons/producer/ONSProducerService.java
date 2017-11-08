package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.rocketmq.producer.MessageSerializer;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;

/**
 * @author wayshall
 * <br/>
 */
public class ONSProducerService extends ProducerBean implements InitializingBean, DisposableBean {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private Consumer<Throwable> errorHandler = null;
	private MessageSerializer messageSerializer = msg->SerializationUtils.serialize((Serializable)msg);
	

	public void setErrorHandler(Consumer<Throwable> errorHandler) {
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

	public SendResult sendBytesMessage(String topic, String tags, byte[] body, Consumer<Throwable> errorHandler){
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
	
	public SendResult sendRawMessage(Message message, Consumer<Throwable> errorHandler){
		try {
			SendResult sendResult = this.send(message);
			logger.info("send message success. sendResult: {}", sendResult);
			return sendResult;
		} catch (ONSClientException e) {
			this.handleException(e, message);
		}catch (Throwable e) {
			this.handleException(e, message);
		}
		return null;
	}
	
	protected void handleException(Throwable e, Message message){
		String errorMsg = "send message error. topic:"+message.getTopic()+", tags:"+message.getTag();
		logger.error(errorMsg);
		if(errorHandler!=null){
			errorHandler.accept(e);
		}else if(e instanceof ONSClientException){
			throw (ONSClientException)e;
		}else{
			throw new ServiceException("发送消息失败", e);
		}
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
	
	@Override
	public void destroy() throws Exception {
		this.shutdown();
	}
	
}
