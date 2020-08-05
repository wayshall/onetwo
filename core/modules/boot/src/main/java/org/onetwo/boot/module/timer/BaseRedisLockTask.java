package org.onetwo.boot.module.timer;

import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author weishao zeng
 * <br/>
 */

public class BaseRedisLockTask implements InitializingBean {
	
	private static final String LOCKER_PREFIX = "TimerJobLock:";

	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	@Autowired
	private RedisLockRegistry redisLockRegistry;
	private String taskLockKey;
	private String taskLockTimeout = "3m";
	
	public BaseRedisLockTask(String taskLockKey) {
		super();
		this.taskLockKey = LOCKER_PREFIX + taskLockKey;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(redisLockRegistry, "redisLockRegistry not found!");
	}
	
	protected void executeTaskInLock(String name, Runnable action) {
		getRedisLockRunner().tryLock(() -> {
			logger.info("开始执行任务[{}]……", name);
			action.run();
			logger.info("结束执行任务[{}]……", name);
			return null;
		}, () -> {
			logger.info("有正在执行的任务，忽略本次任务调度……");
			return null;
		});
	}
	

//    @Scheduled(cron = "${"+MallTimerProperties.AVERAGE_GRADE_JOB_CRON+":0 0 1 * * *}")
    public void doTask() {
    	this.executeTaskInLock("定时更新商品平均评分", ()->{
    	});
    }
    
	protected RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.createLocker(redisLockRegistry, taskLockKey, taskLockTimeout);
		return redisLockRunner;
	}

	public void setTaskLockTimeout(String taskLockTimeout) {
		this.taskLockTimeout = taskLockTimeout;
	}
	
}
