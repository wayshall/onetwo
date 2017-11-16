package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.SerializationUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.MessageSerializer;
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
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

/**
 * ons普通producer和事务produer不能混用
 * @author wayshall
 * <br/>
 */
public class ONSTransactionProducerServiceImpl extends TransactionProducerBean implements InitializingBean, DisposableBean, TransactionProducerService {
	
	public static final LocalTransactionExecuter COMMIT_EXECUTER = (msg, arg)->{
		return TransactionStatus.CommitTransaction;
	};
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private SendMessageErrorHandler<SendResult> errorHandler = null;
	private MessageSerializer messageSerializer = msg->SerializationUtils.serialize((Serializable)msg);

	private ONSProperties onsProperties;
	private String producerId;
	private ONSProducerListenerComposite producerListenerComposite;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
//	private ConfigurableListableBeanFactory configurableListableBeanFactory;
	private FakeProducerService fakeProducerService = new FakeProducerService();
	
	@Autowired
	public void setProducerListenerComposite(ONSProducerListenerComposite producerListenerComposite) {
		this.producerListenerComposite = producerListenerComposite;
	}
	
	@Autowired
	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
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
		
//		this.configurableListableBeanFactory.registerResolvableDependency(ProducerService.class, new ProducerServiceFactory());
	}
	
	@Override
	public void destroy() throws Exception {
		this.shutdown();
	}
	
	public void setErrorHandler(SendMessageErrorHandler<SendResult> errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public SendResult sendMessage(SimpleMessage onsMessage, LocalTransactionExecuter executer, Object arg){
		return sendMessage(onsMessage, executer, arg, errorHandler);
	}
	
	@Override
	public SendResult sendMessage(SimpleMessage onsMessage, LocalTransactionExecuter executer, Object arg, SendMessageErrorHandler<SendResult> errorHandler){
		Message message = onsMessage.toMessage();
		String topic = SpringUtils.resolvePlaceholders(applicationContext, message.getTopic());
		message.setTopic(topic);
		Object body = onsMessage.getBody();
		if(needSerialize(body)){
			message.setBody(this.messageSerializer.serialize(onsMessage.getBody()));
		}else{
			message.setBody((byte[])body);
		}
		return sendRawMessage(message, executer, arg, errorHandler);
	}

	protected boolean needSerialize(Object body){
		if(body==null){
			return false;
		}
		return !byte[].class.isInstance(body);
	}

	protected SendResult sendRawMessage(Message message, LocalTransactionExecuter executer, Object arg, SendMessageErrorHandler<SendResult> errorHandler){
		try {
			producerListenerComposite.beforeSendMessage(message);
			SendResult sendResult = this.send(message, executer, arg);
			producerListenerComposite.afterSendMessage(message, sendResult);
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

	/***
	 * 伪装一个非事务producer，简化调用
	 * 因为ons每个topic只能有一个producer，且事务和非事务producer不能混用
	 * @author wayshall
	 * @return
	 */
	public ProducerService fakeProducerService(){
		return fakeProducerService;
	}
	
	public static class FakeProducerService implements ProducerService {
		@Autowired
		private TransactionProducerService transactionProducerService;

		@Override
		public void sendMessage(String topic, String tags, Object body) {
			SimpleMessage message = SimpleMessage.builder()
												 .topic(topic)
												 .tags(tags)
												 .body(body)
												 .build();
			this.sendMessage(message);
		}

		@Override
		public SendResult sendMessage(SimpleMessage onsMessage) {
			SendResult result = transactionProducerService.sendMessage(onsMessage, COMMIT_EXECUTER, null);
			return result;
		}

		@Override
		public SendResult sendMessage(SimpleMessage onsMessage, SendMessageErrorHandler<SendResult> errorHandler) {
			SendResult result = transactionProducerService.sendMessage(onsMessage, COMMIT_EXECUTER, null, errorHandler);
			return result;
		}
		
	}
	
	/*protected class ProducerServiceFactory implements ObjectFactory<ProducerService> {

		@Override
		public ProducerService getObject() throws BeansException {
			return fakeProducerService();
		}

	}*/
}
