package org.onetwo.ext.ons.producer;

import java.util.ArrayList;

import org.onetwo.boot.mq.SendMessageContext;
import org.slf4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author wayshall
 * <br/>
 */
public class OnsBatchDatabaseTransactionMessageInterceptor extends OnsDatabaseTransactionMessageInterceptor {
	
	private static final NamedThreadLocal<SendMessageEvent> CURRENT_MESSAGES = new NamedThreadLocal<>("ons messages");
	
	@Override
	protected void storeAndPublishSendMessageEvent(SendMessageContext<?> ctx){
		// do nothing
		SendMessageEvent event = CURRENT_MESSAGES.get();
		if (event==null) {
			event = SendMessageEvent.builder()
								 .sendMessageContexts(new ArrayList<>())
								 .batchMode(true)
								 .build();
			CURRENT_MESSAGES.set(event);
			applicationEventPublisher.publishEvent(event);
			Logger log = getLogger();
			if(log.isInfoEnabled()){
				log.info("batch published message event : {}", event);
			}
		}
		this.storeSendMessage(ctx);
	}
	
	public boolean useBatchMode() {
		return true;
	}
	
	protected void save(SendMessageContext<?> ctx) {
		CURRENT_MESSAGES.get().getSendMessageContexts().add(ctx);
	}
	
	@TransactionalEventListener(phase=TransactionPhase.BEFORE_COMMIT)
	public void beforeCommit(SendMessageEvent event) {
		if (event.isBatchMode()) {
			this.getSendMessageRepository().batchSave(event.getSendMessageContexts());
			CURRENT_MESSAGES.remove();
		}
	}

	@Override
	public void afterCommit(SendMessageEvent event){
		CURRENT_MESSAGES.remove();
		super.afterCommit(event);
	}
	
	protected void commitMessages(SendMessageEvent event){
		if (event.isBatchMode()) {
			event.getSendMessageContexts().forEach(ctx -> {
				ctx.getChain().invoke();
			});
//			sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
			getSendMessageRepository().batchUpdateToSent(event.getSendMessageContexts());
			Logger log = getLogger();
			if(log.isInfoEnabled()){
				log.info("batch committed transactional message in thread[{}]...", Thread.currentThread().getId());
			}
		}
	}
	
	@Override
	public void afterRollback(SendMessageEvent event){
		CURRENT_MESSAGES.remove();
		super.afterRollback(event);
	}
}
