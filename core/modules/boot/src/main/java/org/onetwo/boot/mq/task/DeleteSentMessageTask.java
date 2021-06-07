package org.onetwo.boot.mq.task;

import java.time.LocalDateTime;

import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.boot.mq.MQProperties;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.entity.SendMessageEntity.SendStates;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangOps;
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
public class DeleteSentMessageTask implements InitializingBean {
	public static final String LOCK_KEY = "ons:delete_send_message_task";
//	public static final long DEFAULT_RUN_FIXEDRATE = 1000*60*60*1;//每小时运行一次
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	protected BaseEntityManager baseEntityManager;
	
	@Autowired(required=false)
	private RedisLockRegistry redisLockRegistry;
	
	private boolean useReidsLock = true;
	private String redisLockTimeout;
	
	private final int defaultDeleteBeforeAt = 60*60*24*15;//默认删除当前时间15天前的已发送消息
	private String deleteBeforeAt;// example: 15d

	@Override
	public void afterPropertiesSet() throws Exception {
		if(useReidsLock){
			Assert.notNull(redisLockRegistry, "redisLockRegistry not found! ");
		}
	}

	public void setUseReidsLock(boolean useReidsLock) {
		this.useReidsLock = useReidsLock;
	}

	public void setDeleteBeforeAt(String deleteBeforeAt) {
		this.deleteBeforeAt = deleteBeforeAt;
	}

	/***
	 * @author wayshall
	 */
//	@Scheduled(fixedRate=DeleteSentMessageTask.DEFAULT_RUN_FIXEDRATE)
	@Scheduled(cron=MQProperties.TRANSACTIONAL_DELETE_TASK_CRON)
	public void doDeleteSentMessage(){
		log.info("start to check sent message...");
		int deleteCount = 0;
		if(useReidsLock){
			deleteCount = getRedisLockRunner().tryLock(()->{
				return deleteSentMessage(deleteBeforeAt);
			});
		}else{
			deleteCount = deleteSentMessage(deleteBeforeAt);
		}
		if(log.isInfoEnabled()){
			log.info("delete [{}] sent message", deleteCount);
		}
		log.info("finish check sent message...");
	}
	
	
	protected int deleteSentMessage(String deleteBeforeAtString){
		long deleteBeforeAt = LangOps.timeToSeconds(deleteBeforeAtString, defaultDeleteBeforeAt);
		LocalDateTime createAt = LocalDateTime.now().minusSeconds(deleteBeforeAt);
		int deleteCount = Querys.from(baseEntityManager, SendMessageEntity.class)
				.where()
					.field("state").equalTo(SendStates.SENT.ordinal())
					.field("deliverAt").lessThan(createAt)
				.end()
				.toExecute()
				.delete();
		return deleteCount;
	}
	
	private RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.createLocker(redisLockRegistry, LOCK_KEY, redisLockTimeout);
		return redisLockRunner;
	}

	public void setRedisLockTimeout(String redisLockTimeout) {
		this.redisLockTimeout = redisLockTimeout;
	}

}
