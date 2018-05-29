package org.onetwo.boot.module.redis;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class OnceTokenValidator {
	
	@Autowired
	private RedisOperationService redisOperationService;
	
    private int expiredInSeconds = 60*5;
    private String tokenKeyPrefix = "once-token:";
    
    protected String getStoreKey(String key){
    	return tokenKeyPrefix + key;
    }
    
    public String generate(String key, int length){
    	return save(key, ()->LangUtils.getRadomNumberString(length));
    }
    
    public String save(String key, Supplier<String> valueGenerator){
    	String storeKey = getStoreKey(key);
    	String value = valueGenerator.get();
    	redisOperationService.getStringRedisTemplate()
    							.opsForValue()
    							.set(storeKey, value, expiredInSeconds, TimeUnit.SECONDS);
    	return value;
    }
    
    public void checkAndRun(String key, String value, Runnable runnable){
    	String storeKey = getStoreKey(key);
    	String storeValue = redisOperationService.getAndDel(storeKey);
    	if(storeValue==null || !storeValue.equals(value)){
    		throw new ServiceException(RedisErrors.TOKEN_INVALID);
    	}
    	runnable.run();
    }

	public void setRedisOperationService(RedisOperationService redisOperationService) {
		this.redisOperationService = redisOperationService;
	}

	public void setExpiredInSeconds(int expiredInSeconds) {
		this.expiredInSeconds = expiredInSeconds;
	}

	public void setTokenKeyPrefix(String tokenKeyPrefix) {
		this.tokenKeyPrefix = tokenKeyPrefix;
	}

}
