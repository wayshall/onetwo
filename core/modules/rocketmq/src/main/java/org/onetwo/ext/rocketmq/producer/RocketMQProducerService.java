package org.onetwo.ext.rocketmq.producer;

import java.io.Serializable;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.onetwo.boot.mq.exception.MQException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.ext.alimq.MessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


public class RocketMQProducerService implements InitializingBean, DisposableBean {

	final protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String namesrvAddr;
	protected String groupName = "defaultProduerGroup";
	protected DefaultMQProducer defaultMQProducer;
//	private JsonMapper jsonMapper = JsonMapper.defaultMapper();
	private Consumer<Throwable> errorHandler = null;
	protected MessageSerializer messageSerializer = (body, messageDelegate)->SerializationUtils.serialize((Serializable)body);
	private Properties producerProperties;

	public RocketMQProducerService() {
	}
	
	@Autowired
	public void setMessageSerializer(MessageSerializer messageSerializer) {
		this.messageSerializer = messageSerializer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(groupName);
		Assert.hasText(namesrvAddr);
		
		DefaultMQProducer defaultMQProducer = newMQProducer(groupName);
		configMQProducer(defaultMQProducer);
		defaultMQProducer.start();
		this.defaultMQProducer = defaultMQProducer;
	}
	
	protected DefaultMQProducer configMQProducer(DefaultMQProducer defaultMQProducer) {
//		defaultMQProducer.setNamesrvAddr(namesrvAddr);
//		defaultMQProducer.setVipChannelEnabled(false);
		SpringUtils.getMapToBean().injectBeanProperties(producerProperties, defaultMQProducer);
		defaultMQProducer.setNamesrvAddr(namesrvAddr);
		return defaultMQProducer;
	}

	protected DefaultMQProducer newMQProducer(String groupName) {
		return new DefaultMQProducer(groupName);
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
		sendBytesMessage(topic, tags, serializeMessage(body));
	}
	
	public void sendBytesMessage(String topic, String tags, byte[] body){
		SendResult result =  sendBytesMessage(topic, tags, body, errorHandler);
		if(result.getSendStatus()!=SendStatus.SEND_OK){
			throw new MQException("发送消息失败: " + result.getSendStatus());
		}
	}
	
	protected byte[] serializeMessage(Object body) {
		Assert.notNull(messageSerializer);
		return messageSerializer.serialize(body, null);
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
			throw new MQException("send rocketmq message error: " + result.getSendStatus());
		}
	}
	
	public SendResult sendRawMessage(Message message, Consumer<Throwable> errorHandler){
		try {
			SendResult sendResult = this.defaultMQProducer.send(message);
			logger.info("send message success. sendResult: {}", sendResult);
			return sendResult;
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			if (errorHandler!=null) {
				errorHandler.accept(e);
			} else {
				this.handleException(e, message);
			}
		}catch (Throwable e) {
			if (errorHandler!=null) {
				errorHandler.accept(e);
			} else {
				this.handleException(e, message);
			}
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

	public String getNamesrvAddr() {
		return namesrvAddr;
	}

	public String getGroupName() {
		return groupName;
	}

	public DefaultMQProducer getProducer() {
		return defaultMQProducer;
	}

	public Properties getProducerProperties() {
		return producerProperties;
	}

	public void setProducerProperties(Properties producerProperties) {
		this.producerProperties = producerProperties;
	}

	public MessageSerializer getMessageSerializer() {
		return messageSerializer;
	}
	
}
