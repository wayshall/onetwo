package org.onetwo.ext.session;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession(maxInactiveIntervalInSeconds=3600)
@Configuration
public class SessionContextConfig {

    @Resource
    private Properties sessionConfig;
	
	@Bean
	public ConfigrableCookiesHttpSessionStrategy httpSessionStrategy(){
		return new ConfigrableCookiesHttpSessionStrategy();
	}
	
	/*@Bean
    public RedisServerBean redisServer() {
        return new RedisServerBean();
    }*/
	
	/*@Bean
	public Properties sessionConfig() throws Exception{
		PropertiesFactoryBean config = new PropertiesFactoryBean();
		config.setLocation(new ClassPathResource("session-config.properties"));
		config.afterPropertiesSet();
		return config.getObject();
	}*/
	
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() throws Exception {
		int port = Integer.parseInt(sessionConfig.getProperty("port", "6379"));
		String hostName = sessionConfig.getProperty("hostName", "localhost");
		JedisConnectionFactory jf = new JedisConnectionFactory();
		jf.setPort(port);
		jf.setHostName(hostName);
		return jf;
    }
	
   /* class RedisServerBean implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {
        private RedisServer redisServer;


        public void afterPropertiesSet() throws Exception {
        	boolean embeddedRedis = "true".equalsIgnoreCase(sessionConfig.getProperty("redis.embedded", "true"));
        	if(embeddedRedis){
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
//				redisServer = builder.build();//new RedisServer(Protocol.DEFAULT_PORT);
//                redisServer.start();
        		int port = Integer.parseInt(sessionConfig.getProperty("port", "6379"));
        		String path = sessionConfig.getProperty("redis.path");
        		RedisServer redisServer = null;
        		if(StringUtils.isNotBlank(path)){
        			redisServer = new RedisServer(path, port);
        		}else{
        			redisServer = new RedisServer(port);
        		}
        		redisServer.start();
        	}
            final RedisServer rs = redisServer;
            Runtime.getRuntime().addShutdownHook(new Thread(){

				@Override
				public void run() {
					try {
						rs.stop();
					} catch (InterruptedException e) {
						System.out.println("stop redisserver error: " + e.getMessage());
					}
				}
            	
            });
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
    }*/
}
