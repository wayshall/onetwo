package org.onetwo.boot.func.submit;

import java.util.UUID;

import org.onetwo.boot.module.redis.RedisOperationService;
import org.onetwo.boot.module.redis.TokenValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class SubmitTokenService implements InitializingBean {

	private TokenValidator tokenValidator;
	@Autowired
	private RedisOperationService redisOperationService;

	@Override
	public void afterPropertiesSet() throws Exception {
		TokenValidator token = new TokenValidator();
		token.setTokenKeyPrefix("submit-token:");
		token.setExpiredInSeconds(3*60);
		token.setRedisOperationService(redisOperationService);
		this.tokenValidator = token;
	}
	
	public String get() {
		String key = UUID.randomUUID().toString();
		this.tokenValidator.save(key, () -> Boolean.TRUE.toString());
		return key;
	}
	
	public void check(String token) {
		this.tokenValidator.checkOnlyOnce(token, Boolean.TRUE.toString(), () -> true); 
	}
	
	
}

