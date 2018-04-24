package org.onetwo.boot.module.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.boot.module.redis.RedisBaseTest.RedisTtestContextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisTtestContextConfig.class,
				properties={
							"spring.redis.host=localhost",
							"spring.redis.port=6379"
							}
)
public class RedisBaseTest {
	@Autowired
	protected RedisTemplate<Object, Object> redisTemplate;
	
	@Test
	public void testRedisTemplateNotNull(){
		assertThat(redisTemplate).isNotNull();
	}


	@Configuration
	@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, 
			ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, SessionAutoConfiguration.class})
	@Import(RedisConfiguration.class)
	public static class RedisTtestContextConfig {
		
	}
}
