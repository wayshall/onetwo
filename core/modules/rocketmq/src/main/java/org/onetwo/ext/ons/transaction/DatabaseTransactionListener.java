package org.onetwo.ext.ons.transaction;

import java.util.Set;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.ons.producer.SendMessageContext;
import org.onetwo.ext.ons.producer.SendMessageInterceptor;
import org.onetwo.ext.ons.producer.SendMessageInterceptorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.aliyun.openservices.ons.api.SendResult;

/**
 * 应该第一个调用拦截
 * @author wayshall
 * <br/>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseTransactionListener implements SendMessageInterceptor {
	/**
	 * 挂起
	 */
	public static final SendResult SUSPEND = new SendResult();
	static {
		SUSPEND.setMessageId("SUSPEND");
	}
	
	@Autowired
	private SendMessageRepository sendMessageRepository;
	

	@Override
	public SendResult intercept(SendMessageInterceptorChain chain) {
		SendMessageContext ctx = chain.getSendMessageContext();
		//如果是事务producer，则忽略
		if(ctx.getSource().isTransactional()){
			return chain.invoke();
		}
		this.sendMessageRepository.save(ctx);
		return SUSPEND;
	}

	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
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
	}
	
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMPLETION)
	public void afterRollback(){
		sendMessageRepository.clearCurrentContexts();
	}

}
