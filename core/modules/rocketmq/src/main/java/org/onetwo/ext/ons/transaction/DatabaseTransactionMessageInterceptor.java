package org.onetwo.ext.ons.transaction;

import org.onetwo.boot.mq.SendMessageInterceptor;
import org.onetwo.ext.ons.transaction.DefaultDatabaseTransactionMessageInterceptor.SendMessageEvent;
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

}