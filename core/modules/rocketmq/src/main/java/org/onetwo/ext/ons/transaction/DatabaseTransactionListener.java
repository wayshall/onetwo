package org.onetwo.ext.ons.transaction;

import org.onetwo.ext.alimq.ProducerListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public class DatabaseTransactionListener implements ProducerListener {

	@Override
	public void beforeSendMessage(SendMessageContext ctx) {
		storeMessage(message);
	}

	@Override
	public void afterSendMessage(SendMessageContext ctx, SendResult sendResult) {
		//不用处理
	}

	@Override
	public void onSendMessageError(SendMessageContext ctx, Throwable throable) {
		//不用处理
	}
	
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	public void afterCommit(){
		findMessage();
		sendMessage();
	}
	
	@TransactionalEventListener(phase=TransactionPhase.AFTER_ROLLBACK)
	public void afterRollback(){
		//不用处理
	}

}
