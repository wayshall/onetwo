package org.onetwo.boot.module.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wayshall
 * <br/>
 */
public class RedisOperationServiceTest extends RedisBaseTest {
	@Autowired
	@Test
	public void testGetAndDel() throws Exception{
		String key = "test12";
		RedisOperationService ops = new RedisOperationService();
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

}
