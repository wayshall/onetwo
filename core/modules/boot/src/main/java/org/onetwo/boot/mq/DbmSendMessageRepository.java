package org.onetwo.boot.mq;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.boot.mq.SendMessageEntity.SendStates;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DbmSendMessageRepository implements SendMessageRepository {
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
//	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(30);
	@Autowired
	private BaseEntityManager baseEntityManager;
	
//	private NamedThreadLocal<Set<SendMessageContext>> messageStorer = new NamedThreadLocal<>("rmq message thread storer");
	
	@Override
	public void save(SendMessageContext<?> ctx){
		/*Serializable message = ctx.getMessage();
		String key = ctx.getKey();
		if(StringUtils.isBlank(key)){
//			key = String.valueOf(idGenerator.nextId());
			//强制必填，可用于client做idempotent
			throw new ServiceException("message key can not be blank!");
		}
		SendMessageEntity send = createSendMessageEntity(key, message);*/
		
		baseEntityManager.persist(ctx.getMessageEntity());
//		ctx.setMessageEntity(send);

//		storeInCurrentContext(ctx);
	}

	
	@Override
	public void updateToSent(SendMessageContext<?> ctx) {
		boolean debug = ctx.isDebug();
		SendMessageEntity messageEntity = ctx.getMessageEntity();
		messageEntity.setState(SendStates.SENT);
		baseEntityManager.update(messageEntity);
		
		if(debug && log.isInfoEnabled()){
			log.info("update the state of message[{}] to : {}", ctx.getMessageEntity().getKey(), SendStates.SENT);
		}
	}
	
	@Override
	public void updateToSent(SendMessageEntity messageEntity) {
		messageEntity.setState(SendStates.SENT);
		baseEntityManager.update(messageEntity);
	}


	@Override
	public void remove(Collection<SendMessageContext<?>> msgCtxs){
		boolean debug = msgCtxs.iterator().next().isDebug();
		List<String> keys = getSendMessageKeys(msgCtxs);
		baseEntityManager.removeByIds(SendMessageEntity.class, keys.toArray(new String[0]));
		if(debug && log.isInfoEnabled()){
			log.info("remove message data from database: {}", keys);
		}
	}
	
	/*@SuppressWarnings("unchecked")
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
		baseEntityManager.removeByIds(SendMessageEntity.class, keys.toArray(new String[0]));
		if(debug && log.isInfoEnabled()){
			log.info("clear SendMessageContext from database: {}", keys);
		}
	}*/
	

	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	public static List<String> getSendMessageKeys(Collection<SendMessageContext<?>> msgCtxs){
		if(LangUtils.isEmpty(msgCtxs)){
			return Collections.emptyList();
		}
		return msgCtxs.stream().map(ctx->ctx.getMessageEntity().getKey()).collect(Collectors.toList());
	}

}
