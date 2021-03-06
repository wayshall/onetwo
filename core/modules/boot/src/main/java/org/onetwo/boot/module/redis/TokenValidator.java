package org.onetwo.boot.module.redis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class TokenValidator {
	
//	@Autowired
	private RedisOperationService redisOperationService;
	
    private int expiredInSeconds = 60;
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
    	return generate(key, length, expiredInSeconds);
    }
    
    public String generate(String key, int length, int expiredInSeconds){
    	return save(key, expiredInSeconds, ()->RandomStringUtils.randomNumeric(length));
    }
    
    /***
     * 随机生成字母和数字验证码
     * @author weishao zeng
     * @param key
     * @param length
     * @return
     */
    public String generateAlphanumeric(String key, int length){
    	return generateAlphanumeric(key, length, expiredInSeconds);
    }
    public String generateAlphanumeric(String key, int length, int expiredInSeconds){
    	return save(key, expiredInSeconds, ()->RandomStringUtils.randomAlphanumeric(length));
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
    	/*ValueOperations<String, String> ops = redisOperationService.getStringRedisTemplate().opsForValue();
    	String value = ops.get(storeKey);*/
    	Optional<String> valueOpt = this.redisOperationService.getCacheIfPresent(storeKey, String.class);
    	if (valueOpt.isPresent()) {
    		throw new ServiceException(TokenValidatorErrors.TOKEN_NOT_EXPIRED)
    								.put("key", storeKey)
    								.put("token", valueOpt.get());
    	}
    	/*value = valueGenerator.get();
    	redisOperationService.getStringRedisTemplate()
    							.opsForValue()
    							.set(storeKey, value, expiredInSeconds, TimeUnit.SECONDS);*/
    	String value = this.redisOperationService.getCache(storeKey, () -> {
    		return CacheData.<String>builder()
    								.value(valueGenerator.get())
    								.expire(Long.valueOf(expiredInSeconds))
    								.timeUnit(TimeUnit.SECONDS)
    							.build();
    	});
    	return value;
    }
    
    public Long clear(String key) {
    	String cacheKey = this.getStoreKey(key);
    	return this.redisOperationService.clear(cacheKey);
    }
    
    private void checkValue(String value){
    	if(StringUtils.isBlank(value)){
    		throw new ServiceException(TokenValidatorErrors.REQUIRED_VALUE);
    	}
    }
    
    public boolean check(String key, String value, boolean deleteCode){
    	return this.check(key, value, ()->true, ctx->false, deleteCode);
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
     * @param successSupplier
     * @param deleteCode 如果supplier不为null，supplier验证成功后，是否删除code，false为不删除，任其过时。supplier为null时，没有任何作用
     * @return
     */
    public <T> T check(String key, String value, Supplier<T> successSupplier, boolean deleteCode){
    	Function<StoreContext, T> failSupplier = ctx -> {
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID_OR_EXPIRED)
									.put("storeKey", ctx.getStoreKey())
									.put("storeValue", ctx.getStoreValue())
									.put("codeValue", value);
    	};
    	return check(key, value, successSupplier, failSupplier, deleteCode);
    }
    
    public <T> T check(String key, String value, Supplier<T> successSupplier, Function<StoreContext, T> failFunc, boolean deleteCode){
    	this.checkValue(key);
    	this.checkValue(value);
    	String storeKey = getStoreKey(key);
    	/*StringRedisTemplate stringRedisTemplate = redisOperationService.getStringRedisTemplate();
    	String storeValue = stringRedisTemplate.boundValueOps(storeKey).get();*/
    	Optional<String> storeValue = this.redisOperationService.getCacheIfPresent(storeKey, String.class);
    	if (log.isDebugEnabled()) {
    		log.debug("smscode check, request code: {}, store code: {}", value, storeValue.orElse(""));
    	}
    	if(!storeValue.isPresent() || !storeValue.get().equals(value)) {
    		StoreContext ctx = new StoreContext(storeKey, storeValue.orElse(null));
    		return failFunc.apply(ctx);
    	}
    	T res = null;
    	if (successSupplier!=null) {
    		res = successSupplier.get();
        	if (deleteCode) {
        		redisOperationService.clear(storeKey);
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
    		throw new ServiceException(TokenValidatorErrors.TOKEN_INVALID_OR_EXPIRED);
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
    @Data
    @AllArgsConstructor
    public static class StoreContext {
    	private String storeKey;
    	private String storeValue;
    }

}
