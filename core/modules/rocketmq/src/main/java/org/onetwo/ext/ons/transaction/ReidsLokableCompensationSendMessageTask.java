package org.onetwo.ext.ons.transaction;

import org.onetwo.boot.module.redis.RedisLockRunner;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class ReidsLokableCompensationSendMessageTask extends CompensationSendMessageTask {

	public static final String LOCK_KEY = "lock_ons_send_message_task";
	
	@Autowired
	private RedisLockRegistry redisLockRegistry;

	protected void doCheckSendMessage(){
		getRedisLockRunner().tryLock(()->{
			super.doCheckSendMessage();
			return null;
		});
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
