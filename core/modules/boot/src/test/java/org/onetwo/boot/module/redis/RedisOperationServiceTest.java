package org.onetwo.boot.module.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.boot.module.cache.UserEntity;
import org.onetwo.common.concurrent.ConcurrentRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

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
		SimpleRedisOperationService ops = new SimpleRedisOperationService();
		ops.setRedisTemplate(redisTemplate);
		ops.afterPropertiesSet();
		
		StringRedisTemplate stringRedisTemplate = ops.getStringRedisTemplate();
		
		String value = ops.getAndDel(key);
		assertThat(value).isNull();
		
		String testValue = "testValue";
		stringRedisTemplate.opsForValue().set(key, testValue);
		assertThat(stringRedisTemplate.opsForValue().get(key)).isEqualTo(testValue);
		value = ops.getAndDel(key);
		assertThat(value).isEqualTo(testValue);
		assertThat(stringRedisTemplate.opsForValue().get(key)).isNull();
	}
	
	@Test
	public void testCache() {
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
		cacheUser = this.redisOperationService.getCacheIfPreset(key, UserEntity.class).get();
		assertThat(cacheUser).isEqualTo(user);
		assertThat(statis.getMissCount().get()).isEqualTo(1);
		assertThat(statis.getHitCount().get()).isEqualTo(2);

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
