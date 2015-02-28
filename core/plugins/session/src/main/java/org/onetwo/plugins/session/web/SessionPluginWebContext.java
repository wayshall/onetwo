package org.onetwo.plugins.session.web;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.session.SessionPlugin;
import org.onetwo.plugins.session.SessionPluginConfig;
import org.onetwo.plugins.session.SessionPluginConfig.EmbeddedRedisServerConfig;
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

import redis.embedded.RedisServer;


//@Import(EmbeddedRedisConfiguration.class) 
@EnableRedisHttpSession
public class SessionPluginWebContext implements InitializingBean{
	
	@Resource
	private WebApplicationContext webApplicationContext;
	
	private SessionPluginConfig sessionPluginConfig = SessionPlugin.getInstance().getConfig();
	
	
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
		JedisConnectionFactory jf = new JedisConnectionFactory();
		SpringUtils.setMap2Bean(sessionPluginConfig.getRedisConfig(), jf);
		return jf;
    }
	
	@Bean
    public RedisTemplate<String,ExpiringSession> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ExpiringSession> template = new RedisTemplate<String, ExpiringSession>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        if(sessionPluginConfig.isSilentJdkSerializer()){
        	template.setHashValueSerializer(new SilentJdkSerializationRedisSerializer());
        }
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

        private SessionPluginConfig sessionPluginConfig = SessionPlugin.getInstance().getConfig();

        public void afterPropertiesSet() throws Exception {
        	if(sessionPluginConfig.isEmbeddedRedis()){
        		EmbeddedRedisServerConfig redisConfig = sessionPluginConfig.getEmbeddedRedisServerConfig();
        		RedisServer.Builder builder = RedisServer.builder();
        		builder.port(redisConfig.getPort());
        		if(StringUtils.isNotBlank(redisConfig.getExecutable())){
        			builder.executable(redisConfig.getExecutable());
        		}
        		if(StringUtils.isNotBlank(redisConfig.getConfigFile())){
        			builder.executable(redisConfig.getConfigFile());
        		}
        		//0.3自带的ereids版本与现在spring session版本冲突，要设置2.8或以上
//						.executable("D:/mydev/server/redis-2.8.17/redis-server.exe")
//						.configFile("D:/mydev/server/redis-2.8.17/redis.windows.conf")
				redisServer = builder.build();//new RedisServer(Protocol.DEFAULT_PORT);
                redisServer.start();
        	}
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
