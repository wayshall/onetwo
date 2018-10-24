package org.onetwo.boot.mq.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.boot.mq.SendMessageContext;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DbmSendMessageRepository implements SendMessageRepository {
	private static final String LOCK_MESSAGE_SQL = "update data_mq_send set locker=:locker where state = :state and locker='' and deliver_at < :now ";
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
//	private SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(30);
	@Autowired
	private BaseEntityManager baseEntityManager;
	
//	private NamedThreadLocal<Set<SendMessageContext>> messageStorer = new NamedThreadLocal<>("rmq message thread storer");

	@Transactional(propagation=Propagation.REQUIRED)
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
		
		ctx.getMessageEntity().setLocker("");
		baseEntityManager.persist(ctx.getMessageEntity());
//		ctx.setMessageEntity(send);

//		storeInCurrentContext(ctx);
	}


	@Transactional(propagation=Propagation.REQUIRES_NEW)
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

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void updateToSent(SendMessageEntity messageEntity) {
		messageEntity.setState(SendStates.SENT);
		baseEntityManager.update(messageEntity);
	}


	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void remove(Collection<SendMessageContext<?>> msgCtxs){
		boolean debug = msgCtxs.iterator().next().isDebug();
		List<String> keys = getSendMessageKeys(msgCtxs);
		baseEntityManager.removeByIds(SendMessageEntity.class, keys.toArray(new String[0]));
		if(debug && log.isInfoEnabled()){
			log.info("remove message data from database: {}", keys);
		}
	}
	

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public int lockToBeSendMessage(String locker, Date now) {
		int count = baseEntityManager.createQuery(LOCK_MESSAGE_SQL, CUtils.asMap("locker", locker, 
																				"state", SendStates.UNSEND.ordinal(), 
																				"now", now
																				)
												).executeUpdate();
		return count;
	}

	@Transactional(readOnly=true)
	@Override
	public List<SendMessageEntity> findLockerMessage(String locker, Date now, int sendCountPerTask) {
		List<SendMessageEntity> messages = Querys.from(baseEntityManager, SendMessageEntity.class)
				.where()
					.field("state").equalTo(SendStates.UNSEND.ordinal())
					.field("locker").equalTo(locker)
					.field("deliverAt").lessThan(now)
				.end()
				.asc("createAt")
				.limit(0, sendCountPerTask)
//				.lock(LockInfo.write())
				.toQuery()
				.list();
		return messages;
	}


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
