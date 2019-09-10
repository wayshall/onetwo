package org.onetwo.boot.module.redis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import lombok.Setter;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleRedisOperationService implements InitializingBean, RedisOperationService {
	public static final String LOCK_KEY = "LOCKER:";

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	private RedisTemplate<Object, Object> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;
	@Autowired(required=false)
	private RedisLockRegistry redisLockRegistry;
	private String cacheKeyPrefix = DEFAUTL_CACHE_PREFIX;
	private String lockerKey = LOCK_KEY;
	@Setter
    private long waitLockInSeconds = 60;
	@Setter
    private Map<String, Long> expires;
	@Autowired(required=false)
	private RedisCacheManager redisCacheManager;
	final private CacheStatis cacheStatis = new CacheStatis();
    
    @SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate = createReidsTemplate(redisConnectionFactory);
		
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.afterPropertiesSet();
		this.stringRedisTemplate = template;
		
		if (redisCacheManager!=null) {
			expires = (Map<String, Long>) SpringUtils.newPropertyAccessor(redisCacheManager, true).getPropertyValue("expires");
		} else {
			expires = Collections.emptyMap();
		}
	}
    
    protected RedisTemplate<Object, Object> createReidsTemplate(RedisConnectionFactory redisConnectionFactory) {
    	JsonRedisTemplate redisTemplate = new JsonRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
    }

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getRedisLockRunnerByKey(java.lang.String)
	 */
	@Override
	public RedisLockRunner getRedisLockRunnerByKey(String key){
		if (redisLockRegistry==null) {
			throw new IllegalStateException("redisLockRegistry could not be found, Maybe miss config: " + JFishRedisProperties.ENABLED_LOCK_REGISTRY);
		}
		RedisLockRunner redisLockRunner = RedisLockRunner.builder()
														 .lockKey(lockerKey+key)
														 .time(waitLockInSeconds)
														 .unit(TimeUnit.SECONDS)
														 .errorHandler(e->{
															 throw new BaseException("no error hanlder!", e);
														 })
														 .redisLockRegistry(redisLockRegistry)
														 .build();
		return redisLockRunner;
	}
	
	protected String getCacheKey(String key) {
		return cacheKeyPrefix + key;
	}
	
	protected BoundValueOperations<Object, Object> boundValueOperations(String key) {
		return this.redisTemplate.boundValueOps(key);
	}
	

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCacheIfPreset(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> getCacheIfPreset(String key, Class<T> clazz) {
		T value = getCache(key, null);
		return Optional.ofNullable(clazz.cast(value));
	}
	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCache(java.lang.String, java.util.function.Supplier)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCache(String key, Supplier<CacheData<T>> cacheLoader) {
		String cacheKey = getCacheKey(key);
		BoundValueOperations<Object, Object> ops = boundValueOperations(cacheKey);
		Object value = ops.get();
		if (value==null && cacheLoader!=null) {
			CacheData<T> cacheData = getCacheData(ops, cacheKey, cacheLoader);
			if (cacheData==null) {
				throw new BaseException("can not load cache data.").put("key", key);
			}
//			ops.set(cacheData.getValue());
			if (cacheData.getExpire()!=null && cacheData.getExpire()>0) {
//				ops.expire(cacheData.getExpire(), cacheData.getTimeUnit());
				ops.set(cacheData.getValue(), cacheData.getExpire(), cacheData.getTimeUnit());
			} else if (this.expires.containsKey(key)) {
				Long expireInSeconds = this.expires.get(key);
//				ops.expire(expireInSeconds, TimeUnit.SECONDS);
				ops.set(cacheData.getValue(), expireInSeconds, TimeUnit.SECONDS);
			} else {
//				ops.set(cacheData.getValue(), expireInSeconds, TimeUnit.SECONDS);
				ops.set(cacheData.getValue());
			}
			value = cacheData.getValue();
		} else {
			cacheStatis.addHit(1);
		}
		return (T)value;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> CacheData<T> getCacheData(BoundValueOperations<Object, Object> ops, String cacheKey, Supplier<CacheData<T>> cacheLoader) {
		return this.getRedisLockRunnerByKey(cacheKey).tryLock(() -> {
			//double check...
			Object value = ops.get();
			if (value!=null) {
				cacheStatis.addHit(1);
				return CacheData.<T>builder().value((T)value).build();
			}
			cacheStatis.addMiss(1);
			return cacheLoader.get();
		}/*, ()->{
			//如果锁定失败，则休息1秒，然后递归……
			int retryLockInSeconds = 1;
			if(logger.isWarnEnabled()){
				logger.warn("obtain redis lock error, sleep {} seconds and retry...", retryLockInSeconds);
			}
			LangUtils.await(retryLockInSeconds);
			return getCacheData(ops, cacheKey, cacheLoader);
		}*/);
	}
	

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getAndDel(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String getAndDelString(String key){
//    	ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		final String cacheKey = getCacheKey(key);
		RedisSerializer<String> stringSerializer = (RedisSerializer<String>)stringRedisTemplate.getKeySerializer();
//		RedisSerializer<Object> keySerializer = (RedisSerializer<Object>)redisTemplate.getKeySerializer();
    	String value = stringRedisTemplate.execute(new RedisCallback<String>() {

    		public String doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = stringSerializer.serialize(cacheKey);
    			connection.multi();
    			connection.get(rawKey);
    			connection.del(rawKey);
    			List<Object> results = connection.exec();
    			if(LangUtils.isEmpty(results)){
    				return null;
    			}
    			Object result = results.get(0);
    			return stringSerializer.deserialize((byte[])result);
    		}
    		
		});
    	return value;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAndDel(String key){
//    	ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		final String cacheKey = getCacheKey(key);
//		RedisSerializer<String> stringSerializer = (RedisSerializer<String>)stringRedisTemplate.getKeySerializer();
		RedisSerializer<Object> keySerializer = getKeySerializer();
    	T value = this.redisTemplate.execute(new RedisCallback<T>() {

    		public T doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = keySerializer.serialize(cacheKey);
    			connection.multi();
    			connection.get(rawKey);
    			connection.del(rawKey);
    			List<Object> results = connection.exec();
    			if(LangUtils.isEmpty(results)){
    				return null;
    			}
    			Object result = results.get(0);
    			return (T)keySerializer.deserialize((byte[])result);
    		}
    		
		});
    	return value;
    }
	
	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#clear(java.lang.String)
	 */
	@Override
	public Long clear(String key){
		final String cacheKey = getCacheKey(key);
    	Long value = redisTemplate.execute(new RedisCallback<Long>() {

    		public Long doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = getKeySerializer().serialize(cacheKey);
    			Long delCount = connection.del(rawKey);
    			return delCount;
    		}
    		
		});
    	return value;
    }

	@SuppressWarnings("unchecked")
	protected final RedisSerializer<Object> getKeySerializer() {
		return (RedisSerializer<Object>)redisTemplate.getKeySerializer();
	}
	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getRedisTemplate()
	 */
	@Override
	public RedisTemplate<Object, Object> getRedisTemplate() {
		return redisTemplate;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getStringRedisTemplate()
	 */
	@Override
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCacheStatis()
	 */
	@Override
	public CacheStatis getCacheStatis() {
		return cacheStatis;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}

	public void setLockerKey(String lockerKey) {
		this.lockerKey = lockerKey;
	}
	
}
