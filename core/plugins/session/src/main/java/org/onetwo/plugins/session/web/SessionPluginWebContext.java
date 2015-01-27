package org.onetwo.plugins.session.web;

import org.onetwo.plugins.session.utils.EmbeddedRedisConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@Import(EmbeddedRedisConfiguration.class) 
@EnableRedisHttpSession
public class SessionPluginWebContext {
	
	@Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory(); 
    }
	
}
