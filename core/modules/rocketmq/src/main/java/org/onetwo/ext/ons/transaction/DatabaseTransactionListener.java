package org.onetwo.ext.ons.transaction;

import org.onetwo.ext.alimq.ProducerListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public class DatabaseTransactionListener implements ProducerListener<Message> {

	@Override
	public void beforeSendMessage(Message message) {
		storeMessage(message);
	}

	@Override
	public void afterSendMessage(Message message, SendResult sendResult) {
		//不用处理
	}

	@Override
	public void onSendMessageError(Message message, Throwable throable) {
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
