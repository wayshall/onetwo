package org.onetwo.boot.mq.interceptor;

import java.util.List;

import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.common.utils.LangUtils;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public interface DatabaseTransactionMessageInterceptor extends SendMessageInterceptor {
	
	/****
	 * 是否使用批量模式
	 * @author weishao zeng
	 * @return
	 */
	public default boolean useBatchMode() {
		return false;
	}

	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	void afterCommit(SendMessageEvent event);

	@TransactionalEventListener(phase=TransactionPhase.AFTER_ROLLBACK)
	void afterRollback(SendMessageEvent event);

	@Data
	@Builder
	static public class SendMessageEvent {
		private final List<SendMessageContext<?>> sendMessageContexts;
		/***
		 * 是否批量模式
		 */
		private boolean batchMode;

		public boolean isDebug() {
			return getSendMessageContext().isDebug();
		}
		private SendMessageContext<?> getSendMessageContext() {
			SendMessageContext<?> sendMessageContext = LangUtils.isEmpty(sendMessageContexts)?null:sendMessageContexts.get(0);
			return sendMessageContext;
		}
		
	}
}