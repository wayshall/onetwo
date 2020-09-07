package org.onetwo.boot.module.redis;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author weishao zeng
 * <br/>
 */
public interface RedisOperationService {
	String DEFAUTL_CACHE_PREFIX = "ZIFISH:CACHE:";

	RedisLockRunner getRedisLockRunnerByKey(String key);

	<T> Optional<T> getCacheIfPresent(String key, Class<T> clazz);

	/***
	 * 根据key获取缓存，如果还不存在，则上锁执行cacheLoader
	 * @author weishao zeng
	 * @param key
	 * @param cacheLoader
	 * @return
	 */
	<T> T getCache(String key, Supplier<CacheData<T>> cacheLoader);

	String getAndDelString(String key);
	<T> T getAndDel(String key);
	
	Long clear(String key);

	RedisTemplate<Object, Object> getRedisTemplate();

	StringRedisTemplate getStringRedisTemplate();

	CacheStatis getCacheStatis();
	

	boolean setStringNX(String key, String value, int seconds);
	boolean setStringNXEX(String key, String value, int seconds);
	String getString(String key);
	void setString(String key, String value, Long expireInSeconds);
	
	/***
	 * Redis Rpush 命令用于将一个或多个值插入到列表的尾部(最右边)。
如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
	 * @author weishao zeng
	 * @param <T>
	 * @param listName
	 * @param data
	 */
	Long listPushToTail(String listName, Object data);
	
	Boolean zsetAdd(String setName, Object data, double score);
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
	<T> Set<T> zsetRange(String setName, long start, long end);
	
	/***
	 * 直接设置缓存
	 * @author weishao zeng
	 * @param key
	 * @param value
	 */
	void setCache(String key, Object value);
	void setCache(String key, Supplier<CacheData<?>> cacheLoader);

}
