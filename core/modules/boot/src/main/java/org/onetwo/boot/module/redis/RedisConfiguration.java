package org.onetwo.boot.module.redis;

import org.onetwo.boot.module.redis.JFishRedisProperties.LockRegistryProperties;
import org.onetwo.boot.module.redis.JFishRedisProperties.OnceTokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author wayshall
 * <br/>
 */
@EnableConfigurationProperties({JFishRedisProperties.class})
@ConditionalOnClass({JedisConnectionFactory.class, RedisTemplate.class})
@ConditionalOnProperty(name=JFishRedisProperties.ENABLED_KEY, havingValue="true", matchIfMissing=true)
@ConditionalOnBean(JedisConnectionFactory.class)
public class RedisConfiguration {
	
//	private static final String BEAN_REDISCONNECTIONFACTORY = "redisConnectionFactory";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JFishRedisProperties redisProperties;
    
	/*@Bean
    @ConditionalOnMissingBean(name=BEAN_REDISCONNECTIONFACTORY)
	@ConditionalOnProperty(name=JFishRedisProperties.ENABLED_KEY)
    public JedisConnectionFactory redisConnectionFactory() throws Exception {
		JedisConnectionFactory jf = new JedisConnectionFactory();
		CopyUtils.copy(jf, redisProperties);
		if(redisProperties.getPool()!=null){
			jf.setPoolConfig(redisProperties.getPool());
		}
		return jf;
    }*/
	
	/***
	 * 和StringRedisSerializer不同，只有key使用string
	 * @author wayshall
	 * @param jedisConnectionFactory
	 * @return
	 * @throws Exception
	 */
	@Bean
//	@ConditionalOnMissingBean(name="stringKeyRedisTemplate")
	@ConditionalOnProperty(name=JFishRedisProperties.SERIALIZER_KEY, havingValue="stringKey", matchIfMissing=false)
//	@ConditionalOnProperty(name=JFishRedisProperties.ENABLED_KEY, havingValue="true")
	public RedisTemplate<String, Object> stringKeyRedisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) throws Exception  {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(jedisConnectionFactory);
		
		return template;
	}
	
	@Bean
	@ConditionalOnProperty(name=JFishRedisProperties.SERIALIZER_KEY, havingValue="jackson2", matchIfMissing=true)
	public JsonRedisTemplate jsonRedisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) throws Exception  {
		JsonRedisTemplate template = new JsonRedisTemplate();
		template.setConnectionFactory(jedisConnectionFactory);
		return template;
	}
	
	@Bean
	@ConditionalOnProperty(name=JFishRedisProperties.ENABLED_LOCK_REGISTRY)
	@ConditionalOnClass({RedisLockRegistry.class})
	public RedisLockRegistry redisLockRegistry(@Autowired JedisConnectionFactory jedisConnectionFactory){
		LockRegistryProperties lockRegistryProperties = redisProperties.getLockRegistry();
		RedisLockRegistry lockRegistry = new RedisLockRegistry(jedisConnectionFactory, 
																lockRegistryProperties.getKey(), 
																lockRegistryProperties.getExpireAfter());
		return lockRegistry;
	}
	
	@Bean
	public RedisOperationService redisOperationService(){
		return new RedisOperationService();
	}
	
	@Bean
	public TokenValidator tokenValidator(){
		OnceTokenProperties config = this.redisProperties.getOnceToken();
		TokenValidator token = new TokenValidator();
		token.setTokenKeyPrefix(config.getPrefix());
		token.setExpiredInSeconds(config.getExpiredInSeconds());
		return token;
	}
    
}
