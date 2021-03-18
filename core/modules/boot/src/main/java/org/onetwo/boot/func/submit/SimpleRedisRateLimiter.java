package org.onetwo.boot.func.submit;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;
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
	
	public void updateCoinsumeTimes(String actionKey, int times) {
		BoundValueOperations<String, String> ops = operation(actionKey);
		ops.set(String.valueOf(times));
	}

	@Override
	public void consumeAction(ActionContext actionContext) {
		checkActionKey(actionContext.getActionKey());
		
		if (actionContext.getStartTime()!=null && actionContext.getEndTime()!=null) {
			consumeByDuration(actionContext);
		} else {
			consumeByPeriod(actionContext);
		}
	}
	
	private void checkActionKey(String key) {
		if (StringUtils.isBlank(key)) {
			throw new BaseException("actionKey can not be blank");
		}
	}
	
	/***
	 * 根据起止时间计算
	 * @author weishao zeng
	 * @param actionContext
	 */
	private void consumeByDuration(ActionContext actionContext) {
		String actionKey = actionContext.getActionKey();
//		if (StringUtils.isBlank(actionKey)) {
//			actionKey = DateUtils.formatDateByPattern(actionContext.getStartTime(), actionContext.getTimePattern());
//			actionKey = actionKey + "_" + DateUtils.formatDateByPattern(actionContext.getEndTime(), actionContext.getTimePattern());
//		}
		BoundValueOperations<String, String> ops = operation(actionKey);
		long currentTimes = ops.increment(1);
		if (currentTimes==1) {
			// 说明key刚初始化。同时注意，这样设置过期与上面的incr是非原子操作
			ops.expireAt(actionContext.getEndTime());
		} else if (currentTimes > actionContext.getTimesInPeriod()) {
			throw new ServiceException(actionContext.getErrorMessage());
		}
	}
	
	private void consumeByPeriod(ActionContext actionContext) {
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
			BoundValueOperations<String, String> ops = operation(actionContext.getActionKey());
			long currentTimes = ops.increment(1);
			if (currentTimes==1) {
				// 说明key刚初始化。同时注意，这样设置过期与上面的incr是非原子操作
				ops.expire(actionContext.getPeriod(), actionContext.getPeriodUnit());
			} else if (currentTimes > actionContext.getTimesInPeriod()) {
				throw new ServiceException(actionContext.getErrorMessage());
			}
		}
	}
	
	private BoundValueOperations<String, String> operation(String key) {
		BoundValueOperations<String, String> ops = redisOperationService.getStringRedisTemplate()
																.boundValueOps(key);
		return ops;
	}
}

