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
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
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
	private JedisConnectionFactory jedisConnectionFactory;
	private RedisTemplate<Object, Object> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
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
		redisTemplate = createReidsTemplate(jedisConnectionFactory);
		
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(jedisConnectionFactory);
		template.afterPropertiesSet();
		this.stringRedisTemplate = template;
		
		if (redisCacheManager!=null) {
			expires = (Map<String, Long>) SpringUtils.newPropertyAccessor(redisCacheManager, true).getPropertyValue("expires");
		} else {
			expires = Collections.emptyMap();
		}
	}
    
    protected RedisTemplate<Object, Object> createReidsTemplate(JedisConnectionFactory jedisConnectionFactory) {
    	JsonRedisTemplate redisTemplate = new JsonRedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
    }

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getRedisLockRunnerByKey(java.lang.String)
	 */
	@Override
	public RedisLockRunner getRedisLockRunnerByKey(String key){
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
		return getCacheKeyPrefix() + key;
	}
	
	protected BoundValueOperations<Object, Object> boundValueOperations(String key) {
		return this.redisTemplate.boundValueOps(key);
	}
	

	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCacheIfPresent(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> getCacheIfPresent(String key, Class<T> clazz) {
		T value = getCache(key, null);
		return Optional.ofNullable(clazz.cast(value));
	}
	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCache(java.lang.String, java.util.function.Supplier)
	 */
	@Override
	public <T> T getCache(String key, Supplier<CacheData<T>> cacheLoader) {
		String cacheKey = getCacheKey(key);
		BoundValueOperations<Object, Object> ops = boundValueOperations(cacheKey);
		Object value = ops.get();
		if (value==null && cacheLoader!=null) {
			CacheData<T> cacheData = getCacheData(ops, cacheKey, cacheLoader);
			if (cacheData==null) {
				throw new BaseException("can not load cache data.").put("key", key);
			}
			value = cacheData.getValue();
		} else {
			cacheStatis.addHit(1);
		}
		return (T)value;
	}
	
	
	protected <T> CacheData<T> getCacheData(BoundValueOperations<Object, Object> ops, String cacheKey, Supplier<CacheData<T>> cacheLoader) {
		return this.getRedisLockRunnerByKey(cacheKey).tryLock(30L, TimeUnit.SECONDS, () -> {
			//double check...
			Object value = ops.get();
			if (value!=null) {
				cacheStatis.addHit(1);
				return CacheData.<T>builder().value((T)value).build();
			}
			cacheStatis.addMiss(1);
			CacheData<T> cacheData = cacheLoader.get();
			if(logger.isInfoEnabled()){
				logger.info("run cacheLoader for key: {}", cacheKey);
			}
			if (cacheData.getExpire()!=null && cacheData.getExpire()>0) {
//				ops.expire(cacheData.getExpire(), cacheData.getTimeUnit());
				ops.set(cacheData.getValue(), cacheData.getExpire(), cacheData.getTimeUnit());
			} else if (this.expires.containsKey(cacheKey)) {
				Long expireInSeconds = this.expires.get(cacheKey);
				ops.set(cacheData.getValue(), expireInSeconds, TimeUnit.SECONDS);
			} else {
				ops.set(cacheData.getValue());
			}
			
			return cacheData;
		}, null);
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
//    			Object result = results.get(0);
    			byte[] rawValue = (byte[])results.get(0);
    			RedisSerializer<String> valueSerializer = (RedisSerializer<String>)getStringRedisTemplate().getValueSerializer();
    			return valueSerializer.deserialize(rawValue);
    		}
    		
		});
    	return value;
    }
	
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
    			// rawValue
    			byte[] rawValue = (byte[])results.get(0);
    			return (T) getValueSerializer().deserialize(rawValue);
    		}
    		
		});
    	return value;
    }
	
	/****
	 * 如果不存在，则设置
	 * @author weishao zeng
	 * @param key
	 * @param value
	 * @param seconds
	 * @return 设置成功，则返回所设置的值，否则返回已存在的值
	 */
	public boolean setNX(String key, Object value, int seconds) {
		final String cacheKey = getCacheKey(key);
		Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = getKeySerializer().serialize(cacheKey);
    			byte[] rawValue = getValueSerializer().serialize(value);
    			Boolean setOp = connection.setNX(rawKey, rawValue);
    			if (setOp!=null && setOp) {
    				connection.expire(rawKey, seconds);
    			}/* else {
    				rawValue = connection.get(rawKey);
    			}*/
//    			return (T)getValueSerializer().deserialize(rawValue);
    			return setOp;
    		}
    		
		});
    	return result;
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
	@SuppressWarnings("unchecked")
	protected final RedisSerializer<Object> getValueSerializer() {
		return (RedisSerializer<Object>)redisTemplate.getValueSerializer();
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

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}
	
}
