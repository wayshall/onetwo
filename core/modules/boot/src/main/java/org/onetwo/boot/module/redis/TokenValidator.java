package org.onetwo.boot.module.redis;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wayshall
 * <br/>
 */
public class TokenValidator {
	
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
    	return save(key, expiredInSeconds, valueGenerator);
    }
    
    /****
     * 
     * @author wayshall
     * @param key
     * @param expiredInSeconds 有效期
     * @param valueGenerator
     * @return
     */
    public String save(String key, int expiredInSeconds, Supplier<String> valueGenerator){
    	this.checkValue(key);
    	String storeKey = getStoreKey(key);
    	String value = valueGenerator.get();
    	redisOperationService.getStringRedisTemplate()
    							.opsForValue()
    							.set(storeKey, value, expiredInSeconds, TimeUnit.SECONDS);
    	return value;
    }
    
    private void checkValue(String value){
    	if(StringUtils.isBlank(value)){
    		throw new ServiceException(TokenValidatorErrors.REQUIRED_VALUE);
    	}
    }
    
    /****
     * 验证成功才会删除
     * @author wayshall
     * @param key
     * @param value
     * @param runnable
     */
    public void check(String key, String value, Runnable runnable){
    	this.checkValue(key);
    	this.checkValue(value);
    	String storeKey = getStoreKey(key);
    	StringRedisTemplate stringRedisTemplate = redisOperationService.getStringRedisTemplate();
    	String storeValue = stringRedisTemplate.boundValueOps(storeKey).get();
    	if(storeValue==null || !storeValue.equals(value)){
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID);
    	}
    	runnable.run();
    	stringRedisTemplate.delete(storeKey);
    }
    
    /***
     * 不管是否验证成功，都会删除
     * @author wayshall
     * @param key
     * @param value
     * @param runnable
     */
    public void checkOnlyOnce(String key, String value, Runnable runnable){
    	this.checkValue(key);
    	this.checkValue(value);
    	String storeKey = getStoreKey(key);
    	String storeValue = redisOperationService.getAndDel(storeKey);
    	if(storeValue==null || !storeValue.equals(value)){
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID);
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
