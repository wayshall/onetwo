package org.onetwo.common.rocketmq.producer;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.commons.lang3.SerializationUtils;
import org.onetwo.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

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
	private String groupName;
	private DefaultMQProducer defaultMQProducer;

	public RocketMQProducerService() {
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
		defaultMQProducer.setNamesrvAddr(namesrvAddr);
		defaultMQProducer.setVipChannelEnabled(false);
		defaultMQProducer.start();
		this.defaultMQProducer = defaultMQProducer;
	}



	public void sendMessage(String topic, String tags, Serializable body){
		SendResult result =  sendMessage(topic, tags, body, null);
		if(result.getSendStatus()!=SendStatus.SEND_OK){
			throw ServiceException.formatMessage("发送消息失败!(%s)", result.getSendStatus());
		}
	}

	public SendResult sendMessage(String topic, String tags, Serializable body, Consumer<Throwable> errorHandler){
		Message message = new Message();
		message.setTopic(topic);
		message.setTags(tags);
		message.setBody(SerializationUtils.serialize(body));
		return sendMessage(message, errorHandler);
	}
	public void sendMessage(Message message){
		SendResult result = sendMessage(message, null);
		if(result.getSendStatus()!=SendStatus.SEND_OK){
			throw ServiceException.formatMessage("发送消息失败!(%s)", result.getSendStatus());
		}
	}
	
	public SendResult sendMessage(Message message, Consumer<Throwable> errorHandler){
		try {
			SendResult sendResult = this.defaultMQProducer.send(message);
			logger.info("send message success. sendResult: {}", sendResult);
			return sendResult;
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			String errorMsg = "send message error. topic:"+message.getTopic()+", tags:"+message.getTags();
			logger.error(errorMsg);
			if(errorHandler!=null){
				errorHandler.accept(e);
				return null;
			}else{
				throw new ServiceException(errorMsg);
			}
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
