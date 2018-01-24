package org.onetwo.ext.ons.transaction;

import java.util.Arrays;

import lombok.Builder;
import lombok.Data;

import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.producer.SendMessageContext;
import org.onetwo.ext.ons.producer.SendMessageInterceptor;
import org.onetwo.ext.ons.producer.SendMessageInterceptorChain;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * 应该第一个调用拦截
 * @author wayshall
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultDatabaseTransactionMessageInterceptor implements SendMessageInterceptor, DatabaseTransactionMessageInterceptor {
	/**
	 * 挂起
	 */
	public static final SendResult SUSPEND = new SendResult();
	static {
		SUSPEND.setMessageId("SUSPEND");
	}
	
	protected Logger log = ONSUtils.getONSLogger();
	private SendMessageRepository sendMessageRepository;
	protected boolean debug = true;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	

	@Override
	public SendResult intercept(SendMessageInterceptorChain chain) {
		SendMessageContext ctx = chain.getSendMessageContext();
		//如果是事务producer，则忽略
		if(ctx.getSource().isTransactional()){
			return chain.invoke();
		}

		if(debug && log.isInfoEnabled()){
			log.info("start transactional message in thread[{}]...", ctx.getThreadId());
		}
		ctx.setDebug(debug);
		
		this.sendMessageRepository.save(ctx);
		SendMessageEvent event = SendMessageEvent.builder()
												 .sendMessageContext(ctx)
												 .build();
		applicationEventPublisher.publishEvent(event);
		if(debug && log.isInfoEnabled()){
			log.info("publish message event : {}", ctx.getMessageEntity().getKey());
		}
		return SUSPEND;
	}

	@Override
	public void afterCommit(SendMessageEvent event){
		event.getSendMessageContext().getChain().invoke();
		sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
		if(debug && log.isInfoEnabled()){
			log.info("committed transactional message in thread[{}]...", Thread.currentThread().getId());
		}
//		sendMessageRepository.clearInCurrentContext();
	}
	
	@Override
	public void afterRollback(SendMessageEvent event){
		sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
		if(debug && log.isInfoEnabled()){
			log.info("rollback transactional message in thread[{}]...", Thread.currentThread().getId());
		}
//		sendMessageRepository.clearInCurrentContext();
	}

	public SendMessageRepository getSendMessageRepository() {
		return sendMessageRepository;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setSendMessageRepository(SendMessageRepository sendMessageRepository) {
		this.sendMessageRepository = sendMessageRepository;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	@Data
	@Builder
	static public class SendMessageEvent {
		private final SendMessageContext sendMessageContext;
	}
	
/*	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	public void afterCommit(){
		Set<SendMessageContext> contexts = sendMessageRepository.findCurrentSendMessageContext();
		if(LangUtils.isEmpty(contexts)){
			return ;
		}
		
		for(SendMessageContext ctx: contexts){
			//继续执行发送
			ctx.getChain().invoke();
		}
		sendMessageRepository.clearCurrentContexts();
		
		if(debug && log.isInfoEnabled()){
			log.info("committed transactional message in thread[{}]...", Thread.currentThread().getId());
		}
	}
*/	
	/*@TransactionalEventListener(phase=TransactionPhase.AFTER_COMPLETION)
	public void afterRollback(){
		Set<SendMessageContext> contexts = sendMessageRepository.findCurrentSendMessageContext();
		if(LangUtils.isEmpty(contexts)){
			return ;
		}
		sendMessageRepository.clearCurrentContexts();

		if(debug && log.isInfoEnabled()){
			log.info("rollback transactional message in thread[{}]...", Thread.currentThread().getId());
		}
	}*/

}
