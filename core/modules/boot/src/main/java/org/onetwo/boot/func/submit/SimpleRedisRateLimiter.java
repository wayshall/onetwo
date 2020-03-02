package org.onetwo.boot.func.submit;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.exception.ServiceException;
import org.springframework.data.redis.core.BoundValueOperations;

/**
 * 简单redis限流器
 * 
 * @author weishao zeng
 * <br/>
 */
public class SimpleRedisRateLimiter implements RedisRateLimiter {
	
	private RedisOperationService redisOperationService;

	public SimpleRedisRateLimiter(RedisOperationService redisOperationService) {
		super();
		this.redisOperationService = redisOperationService;
	}

	@Override
	public void consumeAction(ActionContext actionContext) {
		// 以前的分支：n秒内只能提交一次
		if (actionContext.getTimesInPeriod()==1) {
			Long expireTime = actionContext.periodUnit.toSeconds(actionContext.getPeriod());
			boolean result = redisOperationService.setStringNXEX(actionContext.getActionKey(), 
														"true", 
														expireTime.intValue());
			if (!result) {
				throw new ServiceException(actionContext.getErrorMessage());
			}
		} else {
			BoundValueOperations<String, String> ops = redisOperationService.getStringRedisTemplate().boundValueOps(actionContext.getActionKey());
			long currentTimes = ops.increment(1);
			if (currentTimes==1) {
				// 说明key刚初始化。同时注意，这样设置过期与上面的incr是非原子操作
				ops.expire(actionContext.getPeriod(), actionContext.getPeriodUnit());
			} else if (currentTimes > actionContext.getTimesInPeriod()) {
				throw new ServiceException(actionContext.getErrorMessage());
			}
		}
	}
}

