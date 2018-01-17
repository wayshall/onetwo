package org.onetwo.ext.ons.transaction;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.alimq.ProducerListener;
import org.onetwo.ext.ons.producer.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class DatabaseTransactionListener implements ProducerListener {
	@Autowired
	private ProducerService producer;
	@Autowired
	private DbmSendMessageRepository sendMessageRepository;

	@Override
	public void beforeSendMessage(SendMessageContext ctx) {
		//如果是事务producer，则忽略
		if(ctx.getSource().isTransactional()){
			return ;
		}
		this.sendMessageRepository.save(ctx);
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
		Set<SendMessageContext> contexts = sendMessageRepository.findCurrentSendMessageContext();
		if(LangUtils.isEmpty(contexts)){
			return ;
		}
		if(producer!=null || producer.isTransactional()){
			return ;
		}
		for(SendMessageContext ctx: contexts){
			Producer producer = ctx.getProducer();
			if(producer==null){
				log.info("no producer found, ignroe send current message.");
				continue;
			}
			producer.send(ctx.getMessage());
		}
	}
	
	@TransactionalEventListener(phase=TransactionPhase.AFTER_ROLLBACK)
	public void afterRollback(){
		//不用处理
	}

}
