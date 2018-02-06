package org.onetwo.ext.ons.transaction;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.ons.ONSUtils;
import org.onetwo.ext.ons.producer.SendMessageContext;
import org.onetwo.ext.ons.transaction.SendMessageEntity.SendStates;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DbmSendMessageRepository implements SendMessageRepository {
	
	private Logger log = ONSUtils.getONSLogger();
	
//	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(30);
	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
//	private NamedThreadLocal<Set<SendMessageContext>> messageStorer = new NamedThreadLocal<>("rmq message thread storer");
	
	@Override
	public void save(SendMessageContext ctx){
		Message message = ctx.getMessage();
		SendMessageEntity send = new SendMessageEntity();
		String key = message.getKey();
		if(StringUtils.isBlank(key)){
//			key = String.valueOf(idGenerator.nextId());
			//强制必填，可用于client做idempotent
			throw new ServiceException("message key can not be blank!");
		}
		send.setKey(key);
		send.setState(SendStates.TO_SEND);
		send.setBody(messageBodyStoreSerializer.serialize(message));
		
		baseEntityManager.persist(send);

		ctx.setMessageEntity(send);
		storeInCurrentContext(ctx);
	}

	@Override
	public void remove(Collection<SendMessageContext> msgCtxs){
		boolean debug = msgCtxs.iterator().next().isDebug();
		List<String> keys = getSendMessageKeys(msgCtxs);
		baseEntityManager.removeByIds(SendMessageEntity.class, keys.toArray(new String[0]));
		if(debug && log.isInfoEnabled()){
			log.info("remove message data from database: {}", keys);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void storeInCurrentContext(SendMessageContext ctx){
		boolean debug = ctx.isDebug();
		Set<SendMessageContext> msgCtxs = (Set<SendMessageContext>)TransactionSynchronizationManager.unbindResourceIfPossible(this);
		if(debug && log.isInfoEnabled()){
			List<String> keys = getSendMessageKeys(msgCtxs);
			log.info("clear old SendMessageContext from transaction resources before store: {}", keys);
		}
		
		Set<SendMessageContext> contexts = findAllInCurrentContext();
		if(contexts==null){
			contexts = new LinkedHashSet<SendMessageContext>();
//			messageStorer.set(contexts);
			TransactionSynchronizationManager.bindResource(this, contexts);
		}
		contexts.add(ctx);
		if(debug && log.isInfoEnabled()){
			log.info("store in current context: {}", ctx.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public Set<SendMessageContext> findAllInCurrentContext(){
//		return messageStorer.get();
		return (Set<SendMessageContext>)TransactionSynchronizationManager.getResource(this);
	}
	
	@SuppressWarnings("unchecked")
	public void clearInCurrentContext(){
		Set<SendMessageContext> msgCtxs = (Set<SendMessageContext>)TransactionSynchronizationManager.unbindResourceIfPossible(this);
		if(LangUtils.isEmpty(msgCtxs)){
			return ;
		}
		
		boolean debug = msgCtxs.iterator().next().isDebug();
		List<String> keys = getSendMessageKeys(msgCtxs);
		if(debug && log.isInfoEnabled()){
			log.info("clear SendMessageContext from transaction resources: {}", keys);
		}
		/*baseEntityManager.removeByIds(SendMessageEntity.class, keys.toArray(new String[0]));
		if(debug && log.isInfoEnabled()){
			log.info("clear SendMessageContext from database: {}", keys);
		}*/
	}
	
	public static List<String> getSendMessageKeys(Collection<SendMessageContext> msgCtxs){
		if(LangUtils.isEmpty(msgCtxs)){
			return Collections.emptyList();
		}
		return msgCtxs.stream().map(ctx->ctx.getMessageEntity().getKey()).collect(Collectors.toList());
	}

}
