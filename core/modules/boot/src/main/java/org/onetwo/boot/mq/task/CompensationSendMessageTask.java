package org.onetwo.boot.mq.task;

import java.io.Serializable;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.boot.mq.MQProperties;
import org.onetwo.boot.mq.MQProperties.SendTaskProps;
import org.onetwo.boot.mq.MessageBodyStoreSerializer;
import org.onetwo.boot.mq.ProducerService;
import org.onetwo.boot.mq.SendMessageEntity;
import org.onetwo.boot.mq.SendMessageEntity.SendStates;
import org.onetwo.boot.mq.SendMessageFlags;
import org.onetwo.boot.mq.SendMessageRepository;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.dialet.DBDialect.LockInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class CompensationSendMessageTask implements InitializingBean {
	public static final String LOCK_KEY = "lock_ons_send_message_task";
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	protected BaseEntityManager baseEntityManager;
	
	@Autowired(required=false)
	private RedisLockRegistry redisLockRegistry;
	
	@Autowired
	private MessageBodyStoreSerializer messageBodyStoreSerializer;
	@Autowired
	private SendMessageRepository sendMessageRepository;
	@Autowired
	private ProducerService<?, ?> producerService;
	
	private boolean useReidsLock;
	@Autowired
	private MQProperties mqProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(useReidsLock){
			Assert.notNull(redisLockRegistry, "redisLockRegistry not found!");
		}
	}

	public void setUseReidsLock(boolean useReidsLock) {
		this.useReidsLock = useReidsLock;
	}

	/***
	 * 定时器运行时间，默认60秒一次
	 * @author wayshall
	 */
//	@Scheduled(cron="${"+ONSProperties.TRANSACTIONAL_TASK_CRON_KEY+":0 0/1 * * * *}")
	@Scheduled(fixedRateString="${"+MQProperties.TRANSACTIONAL_SEND_TASK_FIXED_RATE_STRING_KEY+":60000}")
	public void scheduleCheckSendMessage(){
		SendTaskProps taskProps = this.mqProperties.getTransactional().getSendTask();
		int ignoreCreateAtRecently = (int)LangOps.timeToSeconds(taskProps.getIgnoreCreateAtRecently(), 60);
		doCheckSendMessage(taskProps.getSendCountPerTask(), ignoreCreateAtRecently);
	}

	/***
	 * 扫描指定时间前的100条未发送消息，使用悲观锁
	 * 可扩展为其它类型锁
	 * @author wayshall
	 */
	protected void doCheckSendMessage(int deleteCountPerTask, int ignoreCreateAtRecently){
		log.info("start to check unsend message...");
		if(useReidsLock){
			getRedisLockRunner().tryLock(()->{
				findAndProcessUnsendMessage(deleteCountPerTask, ignoreCreateAtRecently);
				return null;
			});
		}else{
			this.findAndProcessUnsendMessage(deleteCountPerTask, ignoreCreateAtRecently);
		}
		log.info("finish check unsend message...");
	}
	
	protected void findAndProcessUnsendMessage(int sendCountPerTask, int ignoreCreateAtRecently){
		//刚插入到db的消息会在事务提交后发送，所以这里扫描的时间要减去一定的时间，避免把刚插入的消息也发送了
		LocalDateTime createAt = LocalDateTime.now().minusSeconds(ignoreCreateAtRecently);
		List<SendMessageEntity> messages = Querys.from(baseEntityManager, SendMessageEntity.class)
												.where()
													.field("state").equalTo(SendStates.UNSEND.ordinal())
													.field("createAt").lessThan(createAt)
												.end()
												.asc("createAt")
												.limit(0, sendCountPerTask)
												.lock(LockInfo.write())
												.toQuery()
												.list();
		if(LangUtils.isEmpty(messages)){
			if(log.isInfoEnabled()){
				log.info("no unsend mesage found from database");
			}
			return ;
		}
		for(SendMessageEntity message : messages){
			Serializable mqMessage = messageBodyStoreSerializer.deserialize(message.getBody());
			processUnsendMessage(message, mqMessage);
			sendMessageRepository.updateToSent(message);
			if(log.isInfoEnabled()){
				log.info("resend message and remove from database, key: {}", message.getKey());
			}
		}
	}
	
	protected void processUnsendMessage(SendMessageEntity message, Serializable mqMessage){
		producerService.send(mqMessage, SendMessageFlags.DisableDatabaseTransactional);
	}
	

	private RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.builder()
														 .lockKey(LOCK_KEY)
														 .errorHandler(e->new BaseException("refresh token error!", e))
														 .redisLockRegistry(redisLockRegistry)
														 .build();
		return redisLockRunner;
	}
}
