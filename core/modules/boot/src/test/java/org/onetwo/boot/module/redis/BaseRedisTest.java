package org.onetwo.boot.module.redis;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.onetwo.boot.module.redis.BaseRedisTest.RedisTestApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisTestApplication.class,
				properties={
							/*"spring.activemq.in-memory=false", 
							"spring.activemq.pool.enabled=false", 
							"spring.activemq.broker-url=tcp://localhost:61616", */
							}
)
public class BaseRedisTest {
	@Rule
	public OutputCaptureRule outputCapture = new OutputCaptureRule();


	@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, 
			ElasticsearchRestClientAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, SessionAutoConfiguration.class})
	//@EnableJms
	@Configuration
	@Import(RedisConfiguration.class)
	public static class RedisTestApplication {
	
		/*@Bean
		public DataSource dataSource(){
			JFishProperties jp = SpringUtils.loadAsJFishProperties("activemq-test.properties");
			DataSource ds = DataSourceBuilder.create()
											 .url(jp.getProperty("jdbc.url"))
											 .driverClassName(jp.getProperty("jdbc.driverClassName"))
											 .username(jp.getProperty("jdbc.username"))
											 .password(jp.getProperty("jdbc.password"))
											 .build();
			return ds;
		}*/
		
		
	}
}
