package org.onetwo.ext.ons.transaction;

import org.onetwo.ext.alimq.ProducerListener;
import org.onetwo.ext.ons.producer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
public class DatabaseTransactionListener implements ProducerListener {
	@Autowired
	private ProducerService producer;

	@Override
	public void beforeSendMessage(SendMessageContext ctx) {
		//如果是事务producer，则忽略
		if(ctx.getSource().isTransactional()){
			return ;
		}
		storeMessage(ctx.getMessage());
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
