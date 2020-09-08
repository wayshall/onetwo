package org.onetwo.boot.module.redis;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.onetwo.common.convert.Types;
import org.onetwo.common.date.Dates;
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
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.util.Assert;

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
		return Optional.ofNullable(Types.convertValue(value, clazz, null));
	}
	/* (non-Javadoc)
	 * @see org.onetwo.boot.module.redis.RedisOperationService#getCache(java.lang.String, java.util.function.Supplier)
	 */
	@SuppressWarnings("unchecked")
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

	public void setCache(String key, Object value) {
		setCache(key, () -> {
			return CacheData.builder()
							.value(value)
							.build();
		});
	}
	
	public void setCache(String key, Supplier<CacheData<?>> cacheLoader) {
		String cacheKey = getCacheKey(key);
		BoundValueOperations<Object, Object> ops = boundValueOperations(cacheKey);
		
		CacheData<?> cacheData = cacheLoader.get();
		if(logger.isInfoEnabled()){
			logger.info("set cache for key: {}", cacheKey);
		}
		configCacheData(ops, cacheKey, cacheData);
	}
	
	@SuppressWarnings("unchecked")
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
			configCacheData(ops, cacheKey, cacheData);
			
			return cacheData;
		}, null);
	}
	
	/***
	 * 设置cache超时
	 * @author weishao zeng
	 * @param ops
	 * @param cacheKey
	 * @param cacheData
	 */
	private void configCacheData(BoundValueOperations<Object, Object> ops, String cacheKey, CacheData<?> cacheData) {
		if (cacheData.getExpireAt()!=null) {
			TimeUnit unit = TimeUnit.SECONDS;
			Duration d = Dates.between(new Date(), cacheData.getExpireAt());
			Long timeout = d.getSeconds();
			ops.set(cacheData.getValue(), timeout, unit);
		} else if (cacheData.getExpire()!=null && cacheData.getExpire()>0) {
//			ops.expire(cacheData.getExpire(), cacheData.getTimeUnit());
			ops.set(cacheData.getValue(), cacheData.getExpire(), cacheData.getTimeUnit());
		}
		
		if (this.expires.containsKey(cacheKey)) {
			Long expireInSeconds = this.expires.get(cacheKey);
			ops.set(cacheData.getValue(), expireInSeconds, TimeUnit.SECONDS);
		} else {
			ops.set(cacheData.getValue());
		}
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
	 * 非原子操作
	 * @author weishao zeng
	 * @param key
	 * @param value
	 * @param seconds
	 * @return 设置成功，则返回所设置的值，否则返回已存在的值
	 */
	public boolean setStringNX(String key, String value, int seconds) {
		final String cacheKey = getCacheKey(key);
		RedisSerializer<String> keySerializer = this.stringRedisTemplate.getStringSerializer();
		Boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = keySerializer.serialize(cacheKey);
    			byte[] rawValue = keySerializer.serialize(value);
    			Boolean setOp = connection.setNX(rawKey, rawValue);
    			if (setOp!=null && setOp) {
    				connection.expire(rawKey, seconds);
    			}
    			return setOp;
    		}
    		
		});
    	return result;
    }
	
	/****
	 * 实现下面命令：
	 * SET key value NX EX expireTime
	 * 原子操作
	 * @author weishao zeng
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
    public boolean setStringNXEX(String key, String value, int seconds) {
		final String cacheKey = getCacheKey(key);
		RedisSerializer<String> keySerializer = this.stringRedisTemplate.getStringSerializer();
		Boolean result = this.stringRedisTemplate.execute(new RedisCallback<Boolean>() {
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
	   			byte[] rawValue = keySerializer.serialize(value);
	   			byte[][] actualArgs = {
	   					keySerializer.serialize(cacheKey),
	   					rawValue,
	   					keySerializer.serialize("NX"),
	   					keySerializer.serialize("EX"),
	   					keySerializer.serialize(String.valueOf(seconds))
	   			};
				return connection.execute("SET", actualArgs) != null;
			}
   		
		});
        return result;
    }
    
    public String getString(String key) {
		final String cacheKey = getCacheKey(key);
		String result = this.stringRedisTemplate.boundValueOps(cacheKey).get();
        return result;
    }
    
    public void setString(String key, String value, Long expireInSeconds) {
		final String cacheKey = getCacheKey(key);
		if (expireInSeconds!=null) {
			this.stringRedisTemplate.boundValueOps(cacheKey).set(value, expireInSeconds, TimeUnit.SECONDS);
		} else {
			this.stringRedisTemplate.boundValueOps(cacheKey).set(value);
		}
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
	
	public Long listPushToTail(String listName, Object data) {
		Assert.hasText(listName, "list must be has text!");
		BoundListOperations<Object, Object> ops = this.redisTemplate.boundListOps(listName);
		return ops.rightPush(data);
	}
	
	/***
	 * Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。
如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
分数值可以是整数值或双精度浮点数。
如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
当 key 存在但不是有序集类型时，返回一个错误。
	 */
	public Boolean zsetAdd(String setName, Object data, double score) {
		Assert.hasText(setName, "set must be has text!");
		BoundZSetOperations<Object, Object> ops = this.redisTemplate.boundZSetOps(setName);
		return ops.add(data, score);
	}

	/***
	 * Redis Zrange 返回有序集中，指定区间内的成员。
其中成员的位置按分数值递增(从小到大)来排序。
具有相同分数值的成员按字典序(lexicographical order )来排列。
如果你需要成员按
值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * @author weishao zeng
	 * @param setName
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Set<T> zsetRange(String setName, long start, long end) {
		Assert.hasText(setName, "setName must be has text!");
		BoundZSetOperations<Object, T> ops = (BoundZSetOperations<Object, T>)this.redisTemplate.boundZSetOps(setName);
		Set<T> datas = ops.range(start, end);
		return datas;
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
