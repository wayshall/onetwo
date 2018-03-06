package org.onetwo.boot.mq;

import lombok.Builder;
import lombok.Data;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author wayshall
 * <br/>
 */
public interface DatabaseTransactionMessageInterceptor extends SendMessageInterceptor {

	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	void afterCommit(SendMessageEvent event);

	@TransactionalEventListener(phase=TransactionPhase.AFTER_ROLLBACK)
	void afterRollback(SendMessageEvent event);

	@Data
	@Builder
	static public class SendMessageEvent {
		private final SendMessageContext<?> sendMessageContext;
	}
}