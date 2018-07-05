package org.onetwo.boot.module.redis;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * @author wayshall
 * <br/>
 */
public class JsonRedisTemplate extends RedisTemplate<String, Object>{

	public JsonRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	public JsonRedisTemplate() {
		JsonMapper jsonMapper = JsonMapper.ignoreNull();
		ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		
		RedisSerializer<String> keySerializer = new StringRedisSerializer();
		RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		setKeySerializer(keySerializer);
		setValueSerializer(valueSerializer);
		setHashKeySerializer(keySerializer);
		setHashValueSerializer(valueSerializer);
	}
	
}
