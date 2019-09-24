package org.onetwo.boot.module.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wayshall
 * <br/>
 */
public class JsonValueRedisTemplate<T> extends RedisTemplate<String, T>{
	private Class<T> valueType;

	@SuppressWarnings("unchecked")
	public JsonValueRedisTemplate(RedisConnectionFactory connectionFactory) {
		this(connectionFactory, (Class<T>)Object.class);
	}

	public JsonValueRedisTemplate(RedisConnectionFactory connectionFactory, Class<T> valueType) {
		this(valueType);
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	/***
	 * 构造后需要调用setConnectionFactory, afterPropertiesSet
	 * @param valueType
	 */
	public JsonValueRedisTemplate(Class<T> valueType) {
//		this.valueType = (Class<T>)ReflectUtils.getGenricType(getClass(), 0);
		this.valueType = valueType;
		RedisSerializer<String> keySerializer = new StringRedisSerializer();
		RedisSerializer<?> valueSerializer = null;
		if (valueType==null || valueType==Object.class) {
			valueSerializer = RedisUtils.typingJackson2RedisSerializer();
		} else {
			valueSerializer = new Jackson2JsonRedisSerializer<T>(valueType);
		}
		setKeySerializer(keySerializer);
		setValueSerializer(valueSerializer);
		setHashKeySerializer(keySerializer);
		setHashValueSerializer(valueSerializer);
	}

	public Class<T> getValueType() {
		return valueType;
	}
	
}
