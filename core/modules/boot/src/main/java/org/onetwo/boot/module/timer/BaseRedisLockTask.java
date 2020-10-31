package org.onetwo.boot.module.timer;

import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
  Cron表达式是一个字符串，是由空格隔开的6或7个域组成，每一个域对应一个含义（秒 分 时 每月第几天 月 星期 年）其中年是可选字段。
  spring的schedule值支持6个域的表达式，也就是不能设定年，如果超过六个则会报错
  	* : 表示匹配该域的任意值，比如在秒*, 就表示每秒都会触发事件。；

    ? : 只能用在每月第几天和星期两个域。表示不指定值，当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“?”；

    - : 表示范围，例如在分域使用5-20，表示从5分到20分钟每分钟触发一次  

    / : 表示起始时间开始触发，然后每隔固定时间触发一次，例如在分域使用5/20,则意味着5分，25分，45分，分别触发一次.  

    , : 表示列出枚举值。例如：在分域使用5,20，则意味着在5和20分时触发一次。  

    L : 表示最后，只能出现在星期和每月第几天域，如果在星期域使用1L,意味着在最后的一个星期日触发。  

    W : 表示有效工作日(周一到周五),只能出现在每月第几日域，系统将在离指定日期的最近的有效工作日触发事件。注意一点，W的最近寻找不会跨过月份  

    LW : 这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。  

    # : 用于确定每个月第几个星期几，只能出现在每月第几天域。例如在1#3，表示某月的第三个星期日。
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
			logger.info("\n---------------[{}]开始执行任务……", name);
			action.run();
			logger.info("\n---------------[{}]结束执行任务……\n", name);
			return null;
		}, () -> {
			logger.info("[{}]有正在执行的任务，忽略本次任务调度……");
			return null;
		});
	}
	

    public void doTask() {
    	this.executeTaskInLock(taskLockKey, ()->{
    	});
    }
    
	protected RedisLockRunner getRedisLockRunner(){
		RedisLockRunner redisLockRunner = RedisLockRunner.createLocker(redisLockRegistry, taskLockKey, taskLockTimeout);
		return redisLockRunner;
	}

	public void setTaskLockTimeout(String taskLockTimeout) {
		this.taskLockTimeout = taskLockTimeout;
	}

	public String getTaskLockKey() {
		return taskLockKey;
	}
	
}
