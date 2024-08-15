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
 * @author weishao zeng
 * <br/>
 */
final public class RedisUtils {

	static public RedisTemplate<Object, Object> createRedisTemplate(RedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<Object, Object> jsonRedisTemplate = new JsonRedisTemplate(jedisConnectionFactory);
		return jsonRedisTemplate;
	}
	
	static public RedisTemplate<String, Object> createStringRedisTemplate(RedisConnectionFactory jedisConnectionFactory) {
		return createStringRedisTemplate(jedisConnectionFactory, true);
	}
	static public RedisTemplate<String, Object> createStringRedisTemplate(RedisConnectionFactory jedisConnectionFactory, boolean init) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(jedisConnectionFactory);
		if (init) {
			template.afterPropertiesSet();
		}
		return template;
	}
	static public <T> RedisTemplate<String, T> createJsonValueRedisTemplate(RedisConnectionFactory jedisConnectionFactory) {
		JsonValueRedisTemplate<T> template = new JsonValueRedisTemplate<T>(jedisConnectionFactory);
		template.afterPropertiesSet();
		return template;
	}
	
	static public RedisSerializer<Object> typingJackson2RedisSerializer() {
		JsonMapper jsonMapper = JsonMapper.ignoreNull();
		ObjectMapper mapper = jsonMapper.getObjectMapper();
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);//用这个配置，如果写入的对象是list，并且元素是复合对象时，会抛错：Current context not Object but Array
//		mapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT);
		RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		return valueSerializer;
	}
	

    static public JsonRedisTemplate createJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    	JsonRedisTemplate redisTemplate = new JsonRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
    }
    
	private RedisUtils() {
	}

}

