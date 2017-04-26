package org.onetwo.ext.rocketmq.producer;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class RocketMQProducerService implements InitializingBean, DisposableBean {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String namesrvAddr;
	private String groupName = "defaultProduerGroup";
	private DefaultMQProducer defaultMQProducer;
//	private JsonMapper jsonMapper = JsonMapper.defaultMapper();
	private Consumer<Throwable> errorHandler = null;
	private MessageSerializer messageSerializer = msg->SerializationUtils.serialize((Serializable)msg);

	public RocketMQProducerService() {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(groupName);
//		Assert.hasText(namesrvAddr);
		
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
		defaultMQProducer.setNamesrvAddr(namesrvAddr);
		defaultMQProducer.setVipChannelEnabled(false);
		defaultMQProducer.start();
		this.defaultMQProducer = defaultMQProducer;
	}


	public void setErrorHandler(Consumer<Throwable> errorHandler) {
		this.errorHandler = errorHandler;
	}

	/*public void sendJdkSerializedMessage(String topic, String tags, Serializable body){
		sendMessage(topic, tags, SerializationUtils.serialize(body));
	}
	public void sendJsonSerializedMessage(String topic, String tags, Object body){
		sendMessage(topic, tags, jsonMapper.toJsonBytes(body));
	}*/

	public void sendMessage(String topic, String tags, Object body){
		Assert.notNull(messageSerializer);
		sendBytesMessage(topic, tags, messageSerializer.serialize(body));
	}
	
	public void sendBytesMessage(String topic, String tags, byte[] body){
		SendResult result =  sendBytesMessage(topic, tags, body, errorHandler);
		if(result.getSendStatus()!=SendStatus.SEND_OK){
			throw BaseException.formatMessage("发送消息失败!(%s)", result.getSendStatus());
		}
	}

	public SendResult sendBytesMessage(String topic, String tags, byte[] body, Consumer<Throwable> errorHandler){
		Message message = new Message();
		message.setTopic(topic);
		message.setTags(tags);
		message.setBody(body);
		return sendRawMessage(message, errorHandler);
	}
	public void sendRawMessage(Message message){
		SendResult result = sendRawMessage(message, errorHandler);
		if(result.getSendStatus()!=SendStatus.SEND_OK){
			throw BaseException.formatMessage("发送消息失败!(%s)", result.getSendStatus());
		}
	}
	
	public SendResult sendRawMessage(Message message, Consumer<Throwable> errorHandler){
		try {
			SendResult sendResult = this.defaultMQProducer.send(message);
			logger.info("send message success. sendResult: {}", sendResult);
			return sendResult;
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			this.handleException(e, message);
		}catch (Throwable e) {
			this.handleException(e, message);
		}
		return null;
	}
	
	protected void handleException(Throwable e, Message message){
		String errorMsg = "send message error. topic:"+message.getTopic()+", tags:"+message.getTags();
		logger.error(errorMsg);
		if(errorHandler!=null){
			errorHandler.accept(e);
		}else{
			throw new ServiceException(errorMsg, e);
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if(this.defaultMQProducer!=null)
			this.defaultMQProducer.shutdown();
	}


	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
