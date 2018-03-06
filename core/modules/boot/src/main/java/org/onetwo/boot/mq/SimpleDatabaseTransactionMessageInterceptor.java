package org.onetwo.boot.mq;

import java.util.Arrays;

import org.onetwo.boot.core.web.async.AsyncTaskDelegateService;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

/**
 * 应该第一个调用拦截
 * @author wayshall
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleDatabaseTransactionMessageInterceptor implements InitializingBean, SendMessageInterceptor, DatabaseTransactionMessageInterceptor {

	
//	protected boolean debug = true;
	@Autowired
	protected ApplicationEventPublisher applicationEventPublisher;
	@Autowired(required=false)
	private AsyncTaskDelegateService asyncTaskDelegateService;
	private boolean useAsync;
	private SendMessageRepository sendMessageRepository;
	
	public boolean isUseAsync() {
		return useAsync;
	}

	public void setUseAsync(boolean useAsync) {
		this.useAsync = useAsync;
	}

	protected Logger getLogger(){
		return JFishLoggerFactory.getLogger(getClass());
	}

	@Override
	public Object intercept(org.onetwo.boot.mq.SendMessageInterceptorChain chain) {
		SendMessageContext<?> ctx = chain.getSendMessageContext();
		//如果是事务producer，则忽略
		if(ctx.isTransactional()){
			return chain.invoke();
		}
				
		boolean debug = ctx.isDebug();
		if(debug && getLogger().isInfoEnabled()){
			getLogger().info("start transactional message in thread[{}]...", ctx.getThreadId());
		}
		ctx.setDebug(debug);
		
		this.storeAndPublishSendMessageEvent(ctx);
		return MQUtils.DEFAULT_SUSPEND;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(useAsync){
			Assert.notNull(asyncTaskDelegateService, "asyncTaskDelegateService not found!");
		}
	}

	protected void storeAndPublishSendMessageEvent(SendMessageContext<?> ctx){
		this.getSendMessageRepository().save(ctx);
		SendMessageEvent event = SendMessageEvent.builder()
												 .sendMessageContext(ctx)
												 .build();
		applicationEventPublisher.publishEvent(event);

		boolean debug = ctx.isDebug();
		Logger log = getLogger();
		if(debug && log.isInfoEnabled()){
			log.info("publish message event : {}", ctx.getMessageEntity().getKey());
		}
	}

	@Override
	public void afterCommit(SendMessageEvent event){
		if(useAsync){
			asyncTaskDelegateService.run(()->doAfterCommit(event));
		}else{
			this.doAfterCommit(event);
		}
	}
	public void doAfterCommit(SendMessageEvent event){
		boolean debug = event.getSendMessageContext().isDebug();
		event.getSendMessageContext().getChain().invoke();
//		sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
		getSendMessageRepository().updateToSent(event.getSendMessageContext());
		Logger log = getLogger();
		if(debug && log.isInfoEnabled()){
			log.info("committed transactional message in thread[{}]...", Thread.currentThread().getId());
		}
//		sendMessageRepository.clearInCurrentContext();
	}
	
	@Override
	public void afterRollback(SendMessageEvent event){
		if(useAsync){
			asyncTaskDelegateService.run(()->doAfterRollback(event));
		}else{
			this.doAfterRollback(event);
		}
	}
	public void doAfterRollback(SendMessageEvent event){
		boolean debug = event.getSendMessageContext().isDebug();
		getSendMessageRepository().remove(Arrays.asList(event.getSendMessageContext()));
		Logger log = getLogger();
		if(debug && log.isInfoEnabled()){
			log.info("rollback transactional message in thread[{}]...", Thread.currentThread().getId());
		}
//		sendMessageRepository.clearInCurrentContext();
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


}
