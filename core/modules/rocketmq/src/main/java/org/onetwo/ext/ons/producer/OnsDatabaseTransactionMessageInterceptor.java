package org.onetwo.ext.ons.producer;

import java.util.Date;

import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.interceptor.SimpleDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.alimq.ExtMessage;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.MqServerTypes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author wayshall
 * <br/>
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class OnsDatabaseTransactionMessageInterceptor extends SimpleDatabaseTransactionMessageInterceptor {

	@Autowired
	private ONSProperties onsProperties;

	@Override
	protected void sendMessage(SendMessageContext<?> msgContext) {
//		Message onsMessage = (Message)msgContext.getMessage();
		//延迟消息，提交事务后也不发送
		if(msgContext.isDelayMessage()){
			return ;
		}
		boolean debug = msgContext.isDebug();
		msgContext.getChain().invoke();
//		sendMessageRepository.remove(Arrays.asList(msgContext));
		getSendMessageRepository().updateToSent(msgContext);
		Logger log = getLogger();
		if(debug){
			log.info("committed transactional message in thread[{}]...", Thread.currentThread().getId());
		}
	}
	
	@Override
	protected SendMessageEntity createSendMessageEntity(SendMessageContext<?> ctx){
		ExtMessage onsMessage = (ExtMessage)ctx.getMessage();
		SendMessageEntity send = super.createSendMessageEntity(ctx);
		
		if(ctx.isDelayMessage()){
			if(onsProperties.getServerType()==MqServerTypes.ONS){
				//ons本身支持延迟消息，所以直接把deliver时间设置当前即可
				send.setDeliverAt(new Date());
			} else {
				//开源版rocketmq仅支持18个级别的延迟消息，故使用数据库实现
				send.setDeliverAt(new Date(onsMessage.getStartDeliverTime()));
			}
		}
		return send;
	}

	public ONSProperties getOnsProperties() {
		return onsProperties;
	}

}
