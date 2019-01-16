package org.onetwo.ext.ons.task;

import java.time.LocalDateTime;

import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangOps;
import org.onetwo.ext.ons.ONSProperties.DeleteReceiveTask;
import org.onetwo.ext.ons.entity.ReceiveMessageEntity;
import org.onetwo.ext.ons.entity.ReceiveMessageEntity.ConsumeStates;
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
public class DeleteReceiveMessageTask implements InitializingBean {
	public static final String LOCK_KEY = "locker:ons:delete_receive_message_task";
//	public static final long DEFAULT_RUN_FIXEDRATE = 1000*60*60*1;//每小时运行一次
	
	protected Logger log = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	protected BaseEntityManager baseEntityManager;
	
	@Autowired(required=false)
	private RedisLockRegistry redisLockRegistry;
	private String redisLockTimeout;
	
	
	private final int defaultDeleteBeforeAt = 60*60*24*30;//默认删除当前时间30天前的已发送消息
	private String deleteBeforeAt;// example: 30d

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(redisLockRegistry, "redisLockRegistry not found!");
	}

	public void setDeleteBeforeAt(String deleteBeforeAt) {
		this.deleteBeforeAt = deleteBeforeAt;
	}

	/***
	 * @author wayshall
	 */
	@Scheduled(cron=DeleteReceiveTask.DELETE_RECEIVE_TASK_CRON)
	public void doDeleteSentMessage(){
		log.info("start to check receive message...");
		int deleteCount = 0;
		deleteCount = getRedisLockRunner().tryLock(()->{
			return deleteSentMessage(deleteBeforeAt);
		});
		if(log.isInfoEnabled()){
			log.info("delete [{}] sent message", deleteCount);
		}
		log.info("finish check receive message...");
	}
	
	
	protected int deleteSentMessage(String deleteBeforeAtString){
		long deleteBeforeAt = LangOps.timeToSeconds(deleteBeforeAtString, defaultDeleteBeforeAt);
		LocalDateTime createAt = LocalDateTime.now().minusSeconds(deleteBeforeAt);
		int deleteCount = Querys.from(baseEntityManager, ReceiveMessageEntity.class)
				.where()
					.field("state").equalTo(ConsumeStates.CONSUMED.ordinal())
					.field("createAt").lessThan(createAt)
				.end()
				.delete();
		return deleteCount;
	}
	
	private RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.createLoker(redisLockRegistry, LOCK_KEY, redisLockTimeout);
		return redisLockRunner;
	}

	public void setRedisLockTimeout(String redisLockTimeout) {
		this.redisLockTimeout = redisLockTimeout;
	}
	
}
