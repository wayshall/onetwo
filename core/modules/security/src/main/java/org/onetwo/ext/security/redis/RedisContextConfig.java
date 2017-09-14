package org.onetwo.ext.security.redis;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.condition.OnMissingBean;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.context.SecurityContext;

/***
 * set jfish.security.redis to enable redis context
 * @see BootRedisContextConfig
 * @author wayshall
 *
 */
@Configuration
public class RedisContextConfig {
	
//	private static final String REDIS_CONFIG_NAME = "redisConfig";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SecurityConfig securityConfig;
    
    /*@Autowired(required=false)
    private JedisConnectionFactory jedisConnectionFactory;
    
	@Bean
    public JedisConnectionFactory securityRedisConnectionFactory() throws Exception {
    	if(jedisConnectionFactory!=null){
    		return jedisConnectionFactory;
    	}
		int port = securityConfig.getRedis().getPort();
		String hostName = securityConfig.getRedis().getHostName();
		JedisConnectionFactory jf = new JedisConnectionFactory();
		jf.setPort(port);
		jf.setHostName(hostName);
		if(securityConfig.getRedis().getPool()!=null){
			jf.setPoolConfig(securityConfig.getRedis().getPool());
		}
		return jf;
    }*/
    
    @Bean
    @OnMissingBean(JedisConnectionFactory.class)
    public JedisConnectionFactory securityRedisConnectionFactory() throws Exception {
		int port = securityConfig.getRedis().getPort();
		String hostName = securityConfig.getRedis().getHostName();
		JedisConnectionFactory jf = new JedisConnectionFactory();
		jf.setPort(port);
		jf.setHostName(hostName);
		SpringUtils.newBeanWrapper(jf).setPropertyValues(securityConfig.getRedis());
		if(securityConfig.getRedis().getPool()!=null){
			jf.setPoolConfig(securityConfig.getRedis().getPool());
		}
		return jf;
    }
	
	@Bean
	public RedisTemplate<String, SecurityContext> sessionRedisTemplate()  throws Exception  {
		RedisTemplate<String, SecurityContext> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(securityRedisConnectionFactory());
		return template;
	}
	
	@Bean(name=RedisSecurityContextRepository.BEAN_NAME)
	public RedisSecurityContextRepository redisSecurityContextRepository(){
		RedisSecurityContextRepository repo = new RedisSecurityContextRepository();
		repo.setCookieDomain(securityConfig.getCookie().getDomain());
		repo.setCookieName(securityConfig.getCookie().getName());
		repo.setCookiePath(securityConfig.getCookie().getPath());
		return repo;
	}
	
	@Bean
	public RedisClearContextLogoutHandler redisClearContextLogoutHandler(){
		RedisClearContextLogoutHandler handler = new RedisClearContextLogoutHandler();
		handler.setDefaultTargetUrl(securityConfig.getLoginUrl());
		return handler;
	}

}
