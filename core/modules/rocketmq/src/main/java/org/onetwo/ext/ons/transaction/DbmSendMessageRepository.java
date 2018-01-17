package org.onetwo.ext.ons.transaction;

import java.util.LinkedHashSet;
import java.util.Set;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.id.SnowflakeIdGenerator;
import org.onetwo.ext.alimq.ProducerListener.SendMessageContext;
import org.onetwo.ext.ons.transaction.SendMessageEntity.SendStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
public class DbmSendMessageRepository {

	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1000);
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	private NamedThreadLocal<Set<SendMessageContext>> messageStorer = new NamedThreadLocal<>("rmq message thread storer");
	
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
		Set<SendMessageContext> contexts = messageStorer.get();
		if(contexts==null){
			contexts = new LinkedHashSet<SendMessageContext>();
			messageStorer.set(contexts);
		}
		contexts.add(ctx);
	}
	
	public Set<SendMessageContext> findCurrentSendMessageContext(){
		return messageStorer.get();
	}

}
