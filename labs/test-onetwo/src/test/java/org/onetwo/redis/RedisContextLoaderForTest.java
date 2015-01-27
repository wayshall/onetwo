package org.onetwo.redis;

import org.onetwo.common.test.spring.SpringConfigApplicationContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;



//public class RedisContextLoaderForTest extends SpringConfigApplicationContextLoader {
public class RedisContextLoaderForTest extends SpringConfigApplicationContextLoader {
	

	public RedisContextLoaderForTest() {
		super(false);
	}

	@Override
	protected Class<?>[] getClassArray() {
		return new Class<?>[]{RedisConfig.class};
	}

	@Import(EmbeddedRedisConfiguration.class) 
	@Configuration
	public static class RedisConfig {
		
		@Bean
	    public JedisConnectionFactory connectionFactory() {
			JedisConnectionFactory cf = new JedisConnectionFactory(); 
			return cf;
	    }
		
		@Bean
		public RedisTemplate redisTempalte(){
			RedisTemplate template = new RedisTemplate();
			template.setConnectionFactory(connectionFactory());
			return template;
		}
		
	}

}
