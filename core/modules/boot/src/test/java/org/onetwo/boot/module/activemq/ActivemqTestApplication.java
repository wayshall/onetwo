package org.onetwo.boot.module.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author wayshall
 * <br/>
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, 
								ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, SessionAutoConfiguration.class})
@EnableJms
public class ActivemqTestApplication {
	/*@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}*/

	public static void main(String[] args) {
		SpringApplication.run(ActivemqTestApplication.class, args);
	}
}
