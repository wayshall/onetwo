package org.onetwo.boot.module.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.boot.core.web.service.impl.SimpleLoggerManager;
import org.onetwo.boot.module.cache.UserEntity;
import org.onetwo.common.concurrent.ConcurrentRunnable;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import ch.qos.logback.classic.Level;

/**
 * @author wayshall
 * <br/>
 */
public class RedisOperationServiceTest extends RedisBaseTest {
	@Autowired
	SimpleRedisOperationService redisOperationService;
	
	@Test
	public void testGetAndDel() throws Exception{
		String key = "test12";
		SimpleRedisOperationService ops = redisOperationService;
		
		StringRedisTemplate stringRedisTemplate = ops.getStringRedisTemplate();
		
		String value = ops.getAndDel(key);
		assertThat(value).isNull();
		
		String testValue = "testValue";
		stringRedisTemplate.opsForValue().set(key, testValue);
		assertThat(stringRedisTemplate.opsForValue().get(key)).isEqualTo(testValue);
		
		String cacheKeyPrefix = redisOperationService.getCacheKeyPrefix();
		redisOperationService.setCacheKeyPrefix("");
		value = ops.getAndDelString(key);
		assertThat(value).isEqualTo(testValue);
		assertThat(stringRedisTemplate.opsForValue().get(key)).isNull();
		
		redisOperationService.setCacheKeyPrefix(cacheKeyPrefix);
	}
	
	@Test
	public void testSetNX() {
		String key = "testSetNX";
		String testValue = "testValue";
		
		boolean result = redisOperationService.setStringNX(key, testValue, 3);
		assertThat(result).isTrue();
		
		result = redisOperationService.setStringNX(key, testValue, 3);
		assertThat(result).isFalse();
		
		String res = redisOperationService.getString(key);
		assertThat(res).isEqualTo(testValue);
	}
	
	@Test
	public void testSetNXEX() {
		String key = "testSetNXEX";
		String testValue = "testValue";
		
		boolean result = redisOperationService.setStringNXEX(key, testValue, 3);
		assertThat(result).isTrue();
		
		result = redisOperationService.setStringNXEX(key, testValue, 3);
		assertThat(result).isFalse();
		String res = redisOperationService.getString(key);
		assertThat(res).isEqualTo(testValue);
		
		LangUtils.await(3);
		res = redisOperationService.getString(key);
		assertThat(res).isNull();
	}
	
	@Test
	public void testCache() {
		SimpleLoggerManager.getInstance().changeLevel(Level.DEBUG, RedisLockRunner.class);
		SimpleLoggerManager.getInstance().changeLevel(Level.DEBUG, SimpleRedisOperationService.class);
		
		String key = "testCache";
		redisOperationService.getCacheStatis().setEnabled(true);
		redisOperationService.clear(key);
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setUserName("testUserName");
		UserEntity cacheUser = this.redisOperationService.getCache(key, ()->CacheData.<UserEntity>builder().value(user).build());
		assertThat(cacheUser).isEqualTo(user);
		CacheStatis statis = redisOperationService.getCacheStatis();
		assertThat(statis.getMissCount().get()).isEqualTo(1);
		
		cacheUser = this.redisOperationService.getCache(key, ()->CacheData.<UserEntity>builder().value(user).build());
		assertThat(cacheUser).isEqualTo(user);
		cacheUser = this.redisOperationService.getCacheIfPresent(key, UserEntity.class).get();
		assertThat(cacheUser).isEqualTo(user);
		assertThat(statis.getMissCount().get()).isEqualTo(1);
		assertThat(statis.getHitCount().get()).isEqualTo(2);

		System.out.println("=================================================");
		int count = 10;
		redisOperationService.clear(key);
		ConcurrentRunnable.create(count, () -> {
			UserEntity cacheUser2 = this.redisOperationService.getCache(key, ()->CacheData.<UserEntity>builder().value(user).expire(60L).build());
			assertThat(cacheUser2).isEqualTo(user);
		})
		.start()
		.await();
		assertThat(statis.getMissCount().get()).isEqualTo(2);
		assertThat(statis.getHitCount().get()).isEqualTo(2+count-1);
	}

}
