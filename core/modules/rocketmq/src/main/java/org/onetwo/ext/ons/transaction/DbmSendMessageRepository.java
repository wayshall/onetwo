package org.onetwo.ext.ons.transaction;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.id.SnowflakeIdGenerator;
import org.onetwo.ext.ons.producer.SendMessageContext;
import org.onetwo.ext.ons.transaction.SendMessageEntity.SendStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class DbmSendMessageRepository implements SendMessageRepository {

	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1000);
	private MessageBodyStoreSerializer messageBodyStoreSerializer = MessageBodyStoreSerializer.INSTANCE;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
//	private NamedThreadLocal<Set<SendMessageContext>> messageStorer = new NamedThreadLocal<>("rmq message thread storer");
	
	@Override
	public void save(SendMessageContext ctx){
		Message message = ctx.getMessage();
		SendMessageEntity send = new SendMessageEntity();
		String key = message.getKey();
		if(StringUtils.isBlank(key)){
			key = String.valueOf(idGenerator.nextId());
		}
		send.setKey(key);
		send.setState(SendStates.TO_SEND);
		send.setBody(messageBodyStoreSerializer.serialize(message));
		
		baseEntityManager.save(send);
		
		storeInThread(ctx);
	}
	
	protected void storeInThread(SendMessageContext ctx){
		Set<SendMessageContext> contexts = findCurrentSendMessageContext();
		if(contexts==null){
			contexts = new LinkedHashSet<SendMessageContext>();
//			messageStorer.set(contexts);
			TransactionSynchronizationManager.bindResource(this, contexts);
		}
		contexts.add(ctx);
		if(log.isInfoEnabled()){
			log.info("storeInThread: {}", ctx.getMessage());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Set<SendMessageContext> findCurrentSendMessageContext(){
//		return messageStorer.get();
		return (Set<SendMessageContext>)TransactionSynchronizationManager.getResource(this);
	}
	
	@Override
	public void clearCurrentContexts(){
		TransactionSynchronizationManager.unbindResourceIfPossible(this);
		if(log.isInfoEnabled()){
			log.info("clearCurrentContexts");
		}
	}

}
