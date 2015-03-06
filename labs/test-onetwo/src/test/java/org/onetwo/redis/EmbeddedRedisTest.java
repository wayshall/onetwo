package org.onetwo.redis;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=RedisContextLoaderForTest.class )
public class EmbeddedRedisTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private RedisTemplate redisTemplate;
	
	@Test
	public void test(){
		redisTemplate.opsForValue().set("zhang", "hao");  
		System.out.println(redisTemplate.opsForValue().get("zhang"));  
	}

}
