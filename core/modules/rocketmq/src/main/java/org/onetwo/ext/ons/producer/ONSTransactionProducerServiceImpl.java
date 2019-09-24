package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.onetwo.boot.mq.InterceptableMessageSender;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor;
import org.onetwo.boot.mq.interceptor.SendMessageInterceptor.InterceptorPredicate;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.alimq.MessageSerializer.MessageDelegate;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils;
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
public class ONSTransactionProducerServiceImpl extends TransactionProducerBean implements InitializingBean, DisposableBean, DefaultProducerService, TransactionProducerService {
	
	public static final LocalTransactionExecuter COMMIT_EXECUTER = (msg, arg)->{
		return TransactionStatus.CommitTransaction;
	};
	
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private MessageSerializer messageSerializer;

	private ONSProperties onsProperties;
	private String producerId;
//	private ONSProducerListenerComposite producerListenerComposite;
	@Autowired
	private List<SendMessageInterceptor> sendMessageInterceptors;
	private InterceptableMessageSender<SendResult> interceptableMessageSender;
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
//	private ConfigurableListableBeanFactory configurableListableBeanFactory;
	private FakeProducerService fakeProducerService = new FakeProducerService();
	private Logger logger = ONSUtils.getONSLogger();
	
	
	@Autowired
	public void setOnsProperties(ONSProperties onsProperties) {
		this.onsProperties = onsProperties;
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
		Assert.hasText(producerId, "produerId must has text");
		Assert.notNull(onsProperties, "onsProperties can not be null");
		Assert.notNull(messageSerializer, "messageSerializer can not be null");

		this.interceptableMessageSender = new InterceptableMessageSender<SendResult>(sendMessageInterceptors);
		
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
	

	@Override
	public SendResult sendMessage(OnsMessage onsMessage, LocalTransactionExecuter executer, Object arg){
		Object body = onsMessage.getBody();
		if(body instanceof Message){
			return sendRawMessage((Message)body, executer, arg);
		}
		
		Message message = onsMessage.toMessage();
		
		String topic = resolvePlaceholders(message.getTopic());
		message.setTopic(topic);
		String tag = resolvePlaceholders(message.getTag());
		message.setTag(tag);
		
		if(needSerialize(body)){
			message.setBody(this.messageSerializer.serialize(onsMessage.getBody(), new MessageDelegate(message)));
		}else{
			message.setBody((byte[])body);
		}
		configMessage(message, onsMessage);
		
		return sendRawMessage(message, executer, arg);
	}

	protected SendResult sendRawMessage(Message message, LocalTransactionExecuter executer, Object arg){
		return this.sendRawMessage(message, SendMessageFlags.DisableDatabaseTransactional, executer, arg);
	}
	
	protected SendResult sendRawMessage(Message message, InterceptorPredicate interPredicate, LocalTransactionExecuter executer, Object arg){
		/*SendMessageInterceptorChain chain = new SendMessageInterceptorChain(sendMessageInterceptors, null, ()->this.send(message, executer, arg));
		ONSSendMessageContext ctx = ONSSendMessageContext.builder()
													.message(message)
													.source(this)
//													.transactionProducer(this)
													.chain(chain)
													.build();
		chain.setSendMessageContext(ctx);
		return (SendResult)chain.invoke();*/
		
		return this.sendRawMessage(message, interPredicate, () -> doSendRawMessage(message, executer, arg));
	}

	public SendResult doSendRawMessage(Message message, LocalTransactionExecuter executer, Object arg){
		try {
			return send(message, executer, arg);
		} catch (ONSClientException e) {
			handleException(e, message);
		}catch (Throwable e) {
			handleException(e, message);
		}
		return null;
	}
	
	@Override
	public boolean isTransactional() {
		return true;
	}

	@Override
	public <T> T getRawProducer(Class<T> targetClass) {
		return targetClass.cast(this);
	}
	
	protected String resolvePlaceholders(String value){
		return SpringUtils.resolvePlaceholders(applicationContext, value);
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

	public InterceptableMessageSender<SendResult> getInterceptableMessageSender() {
		return interceptableMessageSender;
	}
	
	public class FakeProducerService implements ProducerService {
		@Autowired
		private TransactionProducerService transactionProducerService;


		@Override
		public SendResult send(Serializable onsMessage, InterceptorPredicate interceptorPredicate) {
			if(logger.isWarnEnabled()){
				logger.warn("FakeProducerService is not support InterceptorPredicate arguments, ignored: {}", interceptorPredicate);
			}
			if(onsMessage instanceof Message){
				return sendRawMessage((Message)onsMessage, COMMIT_EXECUTER, null);
			}else if(onsMessage instanceof OnsMessage){
				return sendMessage((OnsMessage)onsMessage, interceptorPredicate);
			}else{
				throw new IllegalArgumentException("error message type: " + onsMessage.getClass());
			}
		}

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
		public SendResult sendMessage(OnsMessage onsMessage) {
			SendResult result = transactionProducerService.sendMessage(onsMessage, COMMIT_EXECUTER, null);
			return result;
		}

		@Override
		public <T> T getRawProducer(Class<T> targetClass) {
			return transactionProducerService.getRawProducer(targetClass);
		}

		@Override
		public boolean isTransactional() {
			return transactionProducerService.isTransactional();
		}

		@Override
		public SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate) {
			if(logger.isWarnEnabled()){
				logger.warn("FakeProducerService is not support InterceptorPredicate arguments, ignored: {}", interceptorPredicate);
			}
			return transactionProducerService.sendMessage(onsMessage, COMMIT_EXECUTER, null);
		}
		
	}
	
	/*protected class ProducerServiceFactory implements ObjectFactory<ProducerService> {

		@Override
		public ProducerService getObject() throws BeansException {
			return fakeProducerService();
		}

	}*/
	
}
