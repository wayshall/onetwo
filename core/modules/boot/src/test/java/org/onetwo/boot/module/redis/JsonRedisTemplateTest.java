package org.onetwo.boot.module.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public class JsonRedisTemplateTest extends BaseRedisTest {
	
	@Autowired
	RedisConnectionFactory connectionFactory;
	
	@Autowired
	private JsonRedisTemplate jsonRedisTemplate;
	
	@Test
	public void testRedisSerializer(){
		RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();
		JsonData data = new JsonData();
		data.setAge(111);
		data.setName("testName");
		byte[] bytes = valueSerializer.serialize(data);
		Object res = valueSerializer.deserialize(bytes);
		assertThat(res.getClass()).isEqualTo(data.getClass());
	}
	
	@Test
	public void testJsonRedisTemplate(){
		String key = "test:data";
		BoundValueOperations<Object, Object> ops = (BoundValueOperations<Object, Object>)jsonRedisTemplate.boundValueOps(key);
		JsonData data = new JsonData();
		data.setAge(111);
		data.setName("testName");
		ops.set(data);
		
		JsonData data2 = (JsonData)ops.get();
		assertThat(data2).isEqualTo(data);
		
		jsonRedisTemplate.delete(key);
	}
	
	@Data
	public static class JsonData {
		String name;
		int age;
	}

}
