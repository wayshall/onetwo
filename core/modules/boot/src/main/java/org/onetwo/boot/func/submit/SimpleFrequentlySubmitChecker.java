package org.onetwo.boot.func.submit;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简单的短时间内频繁提交的检查工具
 * 
 * @author weishao zeng
 * <br/>
 */
public class SimpleFrequentlySubmitChecker implements InitializingBean, FrequentlySubmitChecker {
	
	@Autowired
	private RedisOperationService redisOperationService;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	@Override
	public void checkActionKey(String key) {
		boolean result = redisOperationService.setNX(key, true, 3);
		if (!result) {
			throw new ServiceException("请勿频繁提交！");
		}
	}
}

