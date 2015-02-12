package org.onetwo.plugins.session.web;

import javax.annotation.Resource;

import org.onetwo.plugins.session.utils.SilentJdkSerializationRedisSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.context.WebApplicationContext;

import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;


//@Import(EmbeddedRedisConfiguration.class) 
@EnableRedisHttpSession
public class SessionPluginWebContext implements InitializingBean{
	
	@Resource
	private WebApplicationContext webApplicationContext;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	@Bean
	public HttpSessionStrategy httpSessionStrategy(){
		HttpSessionStrategy s = new JFishCookiesHttpSessionStrategy();
		return s;
	}

	@Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory(); 
    }
	
	@Bean
    public RedisTemplate<String,ExpiringSession> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ExpiringSession> template = new RedisTemplate<String, ExpiringSession>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new SilentJdkSerializationRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }
	
	@Bean
    public RedisServerBean redisServer() {
        return new RedisServerBean();
    }

    /**
     * Implements BeanDefinitionRegistryPostProcessor to ensure this Bean
     * is initialized before any other Beans. Specifically, we want to ensure
     * that the Redis Server is started before RedisHttpSessionConfiguration
     * attempts to enable Keyspace notifications.
     */
    class RedisServerBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {
        private RedisServer redisServer;


        public void afterPropertiesSet() throws Exception {
            redisServer = RedisServer.builder()
            						.port(Protocol.DEFAULT_PORT)
            						.executable("D:/mydev/server/redis-2.8.17/redis-server.exe")
            						.configFile("D:/mydev/server/redis-2.8.17/redis.windows.conf")
            						.build();//new RedisServer(Protocol.DEFAULT_PORT);
            redisServer.start();
            /*final RedisServer rs = redisServer;
            Runtime.getRuntime().addShutdownHook(new Thread(){

				@Override
				public void run() {
					try {
						rs.stop();
					} catch (InterruptedException e) {
						System.out.println("stop redisserver error: " + e.getMessage());
					}
				}
            	
            });*/
        }

        public void destroy() throws Exception {
            if(redisServer != null) {
                redisServer.stop();
            }
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {}

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {}
    }
}
