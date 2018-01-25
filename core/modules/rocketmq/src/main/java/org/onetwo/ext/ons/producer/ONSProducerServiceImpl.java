package org.onetwo.ext.ons.producer;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.ext.alimq.MessageSerializer;
import org.onetwo.ext.alimq.MessageSerializer.MessageDelegate;
import org.onetwo.ext.alimq.OnsMessage;
import org.onetwo.ext.alimq.SimpleMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSUtils.SendMessageFlags;
import org.onetwo.ext.ons.producer.SendMessageInterceptor.InterceptorPredicate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class ONSProducerServiceImpl extends ProducerBean implements InitializingBean, DisposableBean, ProducerService {

//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private MessageSerializer messageSerializer;

	private ONSProperties onsProperties;
	private String producerId;
	
//	private ONSProducerListenerComposite producerListenerComposite;
	@Autowired
	private List<SendMessageInterceptor> sendMessageInterceptors;
	
	@Autowired
	private ApplicationContext applicationContext;
	
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
		Assert.hasText(producerId);
		Assert.notNull(onsProperties);
		Assert.notNull(messageSerializer);
		

		AnnotationAwareOrderComparator.sort(sendMessageInterceptors);
		
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
		return sendMessage(onsMessage, SendMessageFlags.Default);
	}
	
	@Override
	public SendResult sendMessage(OnsMessage onsMessage, InterceptorPredicate interceptorPredicate){
		Object body = onsMessage.getBody();
		if(body instanceof Message){
			return sendRawMessage((Message)body, interceptorPredicate);
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
		
		return sendRawMessage(message, interceptorPredicate);
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
	
	
	protected SendResult sendRawMessage(Message message, final InterceptorPredicate interPredicate){
		InterceptorPredicate interceptorPredicate = interPredicate==null?SendMessageFlags.Default:interPredicate;
		
		List<SendMessageInterceptor> messageInterceptors = Lists.newArrayList(sendMessageInterceptors);
		List<SendMessageInterceptor> increasingInters = interceptorPredicate.getIncreasingInterceptors();
		if(!increasingInters.isEmpty()){
			messageInterceptors.addAll(interceptorPredicate.getIncreasingInterceptors());
			AnnotationAwareOrderComparator.sort(messageInterceptors);
		}
		SendMessageInterceptorChain chain = new SendMessageInterceptorChain(messageInterceptors, 
																			()->this.send(message), 
																			interceptorPredicate);
		
		SendMessageContext ctx = SendMessageContext.builder()
													.message(message)
													.source(this)
													.producer(this)
													.chain(chain)
													.debug(true)
													.threadId(Thread.currentThread().getId())
													.build();
		chain.setSendMessageContext(ctx);
		chain.setDebug(ctx.isDebug());
		
		return chain.invoke();
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
