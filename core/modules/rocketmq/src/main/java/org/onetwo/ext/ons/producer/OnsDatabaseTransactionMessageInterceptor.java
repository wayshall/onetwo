package org.onetwo.ext.ons.producer;

import java.io.Serializable;
import java.util.Date;

import org.onetwo.boot.mq.SendMessageEntity;
import org.onetwo.boot.mq.SimpleDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.ons.ONSProperties;
import org.onetwo.ext.ons.ONSProperties.MqServerTypes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public class OnsDatabaseTransactionMessageInterceptor extends SimpleDatabaseTransactionMessageInterceptor {

	@Autowired
	private ONSProperties onsProperties;

	@Override
	protected void doAfterCommit(SendMessageEvent event){
		Message onsMessage = (Message)event.getSendMessageContext().getMessage();
		//延迟消息，提交事务后也不发送
		if(onsMessage.getStartDeliverTime()>0){
			return ;
		}
		boolean debug = event.getSendMessageContext().isDebug();
		event.getSendMessageContext().getChain().invoke();
//		sendMessageRepository.remove(Arrays.asList(event.getSendMessageContext()));
		getSendMessageRepository().updateToSent(event.getSendMessageContext());
		Logger log = getLogger();
		if(debug && log.isInfoEnabled()){
			log.info("committed transactional message in thread[{}]...", Thread.currentThread().getId());
		}
	}
	
	@Override
	protected SendMessageEntity createSendMessageEntity(String key, Serializable message){
		Message onsMessage = (Message)message; 
		SendMessageEntity send = super.createSendMessageEntity(key, onsMessage);
		
		if(onsProperties.getServerType()==MqServerTypes.ONS){
			//ons本身支持延迟消息，所以直接把deliver时间设置当前即可
			send.setDeliverAt(new Date());
		}else{
			//开源版rocketmq仅支持18个级别的延迟消息，故使用数据库实现
			if(onsMessage.getStartDeliverTime()>0){
				send.setDeliverAt(new Date(onsMessage.getStartDeliverTime()));
			}else{
				send.setDeliverAt(new Date());
			}
		}
		return send;
	}

}
