package org.onetwo.boot.module.redis;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * @author wayshall
 * <br/>
 */
public class JsonRedisTemplate extends RedisTemplate<Object, Object> {

	public JsonRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	public JsonRedisTemplate() {
		JsonMapper jsonMapper = JsonMapper.ignoreNull();
		ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);//用这个配置，如果写入的对象是list，并且元素是复合对象时，会抛错：Current context not Object but Array
//		mapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT);
		
//		RedisSerializer<String> keySerializer = new StringRedisSerializer();
		RedisSerializer<Object> keySerializer = new GenericJackson2JsonRedisSerializer(mapper);
		RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		setKeySerializer(keySerializer);
		setValueSerializer(valueSerializer);
		setHashKeySerializer(keySerializer);
		setHashValueSerializer(valueSerializer);
	}
	
}
