package org.onetwo.boot.module.redis;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author wayshall
 * <br/>
 */
public class RedisOperationService implements InitializingBean {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
    private StringRedisTemplate stringRedisTemplate;
    
    
    @Override
	public void afterPropertiesSet() throws Exception {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisTemplate.getConnectionFactory());
		template.afterPropertiesSet();
		this.stringRedisTemplate = template;
	}

	@SuppressWarnings("unchecked")
	public String getAndDel(String key){
//    	ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		RedisSerializer<String> stringSerializer = (RedisSerializer<String>)stringRedisTemplate.getKeySerializer();
    	String value = stringRedisTemplate.execute(new RedisCallback<String>() {

    		public String doInRedis(RedisConnection connection) throws DataAccessException {
    			byte[] rawKey = stringSerializer.serialize(key);
    			connection.multi();
    			connection.get(rawKey);
    			connection.del(rawKey);
    			List<Object> results = connection.exec();
    			if(LangUtils.isEmpty(results)){
    				return null;
    			}
    			Object result = results.get(0);
    			return stringSerializer.deserialize((byte[])result);
    		}
    		
		});
    	return value;
    }

	public RedisTemplate<Object, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
