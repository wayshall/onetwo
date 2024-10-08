package org.onetwo.boot.mq.interceptor;

import java.util.Arrays;
import java.util.Date;

import org.onetwo.boot.core.web.async.AsyncTaskDelegateService;
import org.onetwo.boot.mq.MQProperties.SendMode;
import org.onetwo.boot.mq.MQProperties.TransactionalProps;
import org.onetwo.boot.mq.MQUtils;
import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.boot.mq.serializer.MessageBodyStoreSerializer;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.Assert;

/**
 * 应该是最后一个调用拦截
 * 基于本地数据库实现的事务消息：
 * 1、调用封装后的producer.send发送消息，经过此拦截器时，不调用chain.invoke()进行发送，直接返回MQUtils.DEFAULT_SUSPEND
 * 2、MQUtils.DEFAULT_SUSPEND此状态用于基于本地数据库实现的事务消息时，调用producer发送消息时，实际不会发送，只存到了数据库
 * 3、待事务提交成功后，同事务监听器发送或回滚消息
 * @author wayshall
 * <br/>
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class SimpleDatabaseTransactionMessageInterceptor implements InitializingBean, SendMessageInterceptor, DatabaseTransactionMessageInterceptor {

	
//	protected boolean debug = true;
	@Autowired
	protected ApplicationEventPublisher applicationEventPublisher;
	@Autowired(required=false)
	private AsyncTaskDelegateService asyncTaskDelegateService;
//	private boolean useAsync = false;
	private SendMessageRepository sendMessageRepository;
	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	
	private TransactionalProps transactionalProps;
	
	public boolean isUseAsync() {
		return transactionalProps!=null && transactionalProps.getSendMode()==SendMode.ASYNC;
	}

	/*public void setUseAsync(boolean useAsync) {
		this.useAsync = useAsync;
	}*/

	protected Logger getLogger(){
		return JFishLoggerFactory.getLogger(getClass());
	}

	@Override
	public Object intercept(org.onetwo.boot.mq.interceptor.SendMessageInterceptorChain chain) {
		SendMessageContext<?> ctx = chain.getSendMessageContext();
		//如果是ons（rocketmq）的事务producer，则忽略后面的逻辑，使用rocketmq本身的事务消息
		if(ctx.isTransactional()){
			return chain.invoke();
		}
				
		// 否则，执行自定义的基于本地数据库实现的事务消息
		boolean debug = ctx.isDebug();
		if(debug){
			getLogger().info("start transactional message in thread[{}]...", ctx.getThreadId());
		}
		
		this.storeAndPublishSendMessageEvent(ctx);
		return MQUtils.DEFAULT_SUSPEND;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(isUseAsync()){
			Assert.notNull(asyncTaskDelegateService, "asyncTaskDelegateService not found!");
		}
	}

	protected void storeAndPublishSendMessageEvent(SendMessageContext<?> ctx){
		this.storeSendMessage(ctx);
		SendMessageEvent event = SendMessageEvent.builder()
												 .sendMessageContexts(Arrays.asList(ctx))
												 .build();
		applicationEventPublisher.publishEvent(event);

		boolean debug = ctx.isDebug();
		Logger log = getLogger();
		if(debug){
			log.info("publish message event : {}", ctx.getMessageEntity().getKey());
		}
	}

	protected SendMessageEntity storeSendMessage(SendMessageContext<?> ctx){
//		Serializable message = ctx.getMessage();
		String key = ctx.getKey();
		if(StringUtils.isBlank(key)){
//			key = String.valueOf(idGenerator.nextId());
			//强制必填，可用于client做idempotent
			throw new ServiceException("message key can not be blank!");
		}
		SendMessageEntity send = createSendMessageEntity(ctx);
		ctx.setMessageEntity(send);
		save(ctx);
		return send;
	}
	
	/****
	 * 保存消息
	 * @author weishao zeng
	 * @param ctx
	 */
	protected void save(SendMessageContext<?> ctx) {
		this.getSendMessageRepository().save(ctx);
	}
	
	protected SendMessageEntity createSendMessageEntity(SendMessageContext<?> ctx){
		SendMessageEntity send = new SendMessageEntity();
		send.setKey(ctx.getKey());
		send.setState(SendStates.UNSEND);
		send.setBody(messageBodyStoreSerializer.serialize(ctx.getMessage()));
		send.setDeliverAt(new Date());
		send.setDelay(ctx.isDelayMessage());
		return send;
	}

	/****
	 * 注意：
	 * TransactionalEventListener 由 TransactionSynchronizationUtils#invokeAfterCompletion 触发，即使抛错了，也不会中止程序执行
	 */
	@Override
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	public void afterCommit(SendMessageEvent event){
		Logger log = getLogger();
		if (log.isInfoEnabled()) {
			log.info("will send message after transaction committed, message size: {}", LangUtils.size(event.getSendMessageContexts()));
		}
		if (LangUtils.isEmpty(event.getSendMessageContexts())) {
			return ;
		}
		if(isUseAsync()){
			asyncTaskDelegateService.run(()->commitMessages(event));
		}else{
			this.commitMessages(event);
		}
	}
	
	protected void commitMessages(SendMessageEvent event){
		event.getSendMessageContexts().forEach(ctx -> sendMessage(ctx));
	}
	
	protected void sendMessage(SendMessageContext<?> msgContext){
		msgContext.getChain().invoke();
//		sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
		getSendMessageRepository().updateToSent(msgContext);
		Logger log = getLogger();
		if (log.isInfoEnabled()) {
			log.info("message was sent in thread[{}]...", Thread.currentThread().getId());
		}
	}
	
	@Override
	@TransactionalEventListener(phase=TransactionPhase.AFTER_ROLLBACK)
	public void afterRollback(SendMessageEvent event){
		Logger log = getLogger();
		if (log.isInfoEnabled()) {
			log.info("committed message after transaction rollback, message size: {}", LangUtils.size(event.getSendMessageContexts()));
		}
		if (LangUtils.isEmpty(event.getSendMessageContexts())) {
			return ;
		}
		/*if(isUseAsync()){
			asyncTaskDelegateService.run(()->rollbackMessages(event));
		}else{
			this.rollbackMessages(event);
		}*/
		this.rollbackMessages(event);
	}
	
	public void rollbackMessages(SendMessageEvent event){
//		getSendMessageRepository().remove(event.getSendMessageContexts());
		Logger log = getLogger();
		if(log.isInfoEnabled()){
			log.info("rollback transaction in thread[{}]...", Thread.currentThread().getId());
		}
//		sendMessageRepository.clearInCurrentContext();
	}

	public MessageBodyStoreSerializer getMessageBodyStoreSerializer() {
		return messageBodyStoreSerializer;
	}

	public SendMessageRepository getSendMessageRepository() {
		return sendMessageRepository;
	}

	public void setSendMessageRepository(SendMessageRepository sendMessageRepository) {
		this.sendMessageRepository = sendMessageRepository;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public void setTransactionalProps(TransactionalProps transactionalProps) {
		this.transactionalProps = transactionalProps;
	}

}
