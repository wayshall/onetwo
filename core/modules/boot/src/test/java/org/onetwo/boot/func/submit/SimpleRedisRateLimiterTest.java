package org.onetwo.boot.func.submit;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.func.submit.RedisRateLimiter.ActionContext;
import org.onetwo.boot.module.redis.RedisBaseTest;
import org.onetwo.boot.module.redis.SimpleRedisOperationService;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */

public class SimpleRedisRateLimiterTest extends RedisBaseTest {
	
	SimpleRedisRateLimiter redisSubmitRateLimiter;
	@Autowired
	SimpleRedisOperationService redisOperationService;
	
	@Before
	public void setup() {
		redisSubmitRateLimiter = new SimpleRedisRateLimiter(redisOperationService);
	}
	
	@Test
	public void testLimit() {
		ActionContext actionContext = ActionContext.builder()
												.actionKey("testLimit")
												.period(3)
												.timesInPeriod(2)
												.build();
		this.redisSubmitRateLimiter.consumeAction(actionContext);
		LangUtils.await(1);
		this.redisSubmitRateLimiter.consumeAction(actionContext);
		LangUtils.await(1);
		Assertions.assertThatExceptionOfType(ServiceException.class)
				.isThrownBy(() -> {
					this.redisSubmitRateLimiter.consumeAction(actionContext);
				})
				.withMessage(actionContext.getErrorMessage());
		

		LangUtils.await(actionContext.getPeriod());
		this.redisSubmitRateLimiter.consumeAction(actionContext);
		this.redisSubmitRateLimiter.consumeAction(actionContext);
	}

}
