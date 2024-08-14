package org.onetwo.boot.module.cache;

import org.onetwo.boot.core.EnableJFishBootExtension;
import org.onetwo.dbm.spring.EnableDbm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

/**
 * @author wayshall
 * <br/>
 */
@SpringBootApplication(scanBasePackageClasses=JetCacheApplication.class,
						exclude={RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, 
								ElasticsearchRestClientAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, SessionAutoConfiguration.class})
@Configuration
public class JetCacheApplication {
	/*@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}*/

	public static void main(String[] args) {
		SpringApplication.run(JetCacheApplication.class, args);
	}
	

	@Configuration
//	@PropertySource("classpath:activemq-test.properties")
	@ComponentScan
	@EnableDbm
	@EnableMethodCache(basePackages="org.onetwo.boot.module.cache")
	@EnableCreateCacheAnnotation
	@EnableJFishBootExtension
	public static class TestContext {
	}
	
	
}
