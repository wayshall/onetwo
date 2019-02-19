package org.onetwo.boot.module.redis;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author wayshall
 * <br/>
 */
public class TokenValidator {
	
	@Autowired
	private RedisOperationService redisOperationService;
	
    private int expiredInSeconds = 60*2;
    private String tokenKeyPrefix = "once-token:";
    
    protected String getStoreKey(String key){
    	return tokenKeyPrefix + key;
    }
    
    /****
     * 随机生成数字验证码
     * @author weishao zeng
     * @param key
     * @param length
     * @return
     */
    public String generate(String key, int length){
    	return save(key, ()->RandomStringUtils.randomNumeric(length));
    }
    
    /***
     * 随机生成字母和数字验证码
     * @author weishao zeng
     * @param key
     * @param length
     * @return
     */
    public String generateAlphanumeric(String key, int length){
    	return save(key, ()->RandomStringUtils.randomAlphanumeric(length));
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
    	ValueOperations<String, String> ops = redisOperationService.getStringRedisTemplate().opsForValue();
    	String value = ops.get(storeKey);
    	if (StringUtils.isNotBlank(value)) {
    		throw new ServiceException(TokenValidatorErrors.TOKEN_NOT_EXPIRED)
    								.put("key", storeKey)
    								.put("token", value);
    	}
    	value = valueGenerator.get();
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
    
    public void check(String key, String value, Runnable runnable){
    	this.check(key, value, () -> {
    		runnable.run();
    		return null;
    	});
    }
    
    /****
     * 验证成功才会删除
     * @author wayshall
     * @param key
     * @param value
     * @param supplier
     */
    public <T> T check(String key, String value, Supplier<T> supplier){
    	return check(key, value, supplier, false);
    }
    
    /****
     * 
     * @author weishao zeng
     * @param key
     * @param value
     * @param supplier
     * @param deleteCode 如果supplier不为null，supplier验证成功后，是否删除code，false为不删除，任其过时。supplier为null时，没有任何作用
     * @return
     */
    public <T> T check(String key, String value, Supplier<T> supplier, boolean deleteCode){
    	this.checkValue(key);
    	this.checkValue(value);
    	String storeKey = getStoreKey(key);
    	StringRedisTemplate stringRedisTemplate = redisOperationService.getStringRedisTemplate();
    	String storeValue = stringRedisTemplate.boundValueOps(storeKey).get();
    	if(storeValue==null || !storeValue.equals(value)){
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID);
    	}
    	T res = null;
    	if (supplier!=null) {
    		res = supplier.get();
        	if (deleteCode) {
        		stringRedisTemplate.delete(storeKey);
        	}
    	}
    	return res;
    }
    
    public void checkOnlyOnce(String key, String value, Runnable runnable){
    	checkOnlyOnce(key, value, () -> {
    		runnable.run();
    		return null;
    	});
    }
    /***
     * 不管是否验证成功，都会删除
     * @author wayshall
     * @param key
     * @param value
     * @param supplier
     */
    public <T> T checkOnlyOnce(String key, String value, Supplier<T> supplier){
    	this.checkValue(key);
    	this.checkValue(value);
    	String storeKey = getStoreKey(key);
    	String storeValue = redisOperationService.getAndDel(storeKey);
    	if(storeValue==null || !storeValue.equals(value)){
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID);
    	}
    	return supplier.get();
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
