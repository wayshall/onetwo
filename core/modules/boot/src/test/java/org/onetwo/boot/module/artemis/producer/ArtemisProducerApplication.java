package org.onetwo.boot.module.artemis.producer;

import javax.sql.DataSource;

import org.onetwo.boot.module.activemq.ActivemqConfiguration;
import org.onetwo.boot.module.activemq.producer.ActivemqProducerApplication.ActivemqTestContext;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.spring.EnableDbm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wayshall
 * 
 * ArtemisAutoConfiguration
 * <br/>
 */
@SpringBootApplication(
		exclude={
				DataSourceAutoConfiguration.class, 
				RedisAutoConfiguration.class, 
				RedisRepositoriesAutoConfiguration.class, 
				ElasticsearchRestClientAutoConfiguration.class, 
				ElasticsearchDataAutoConfiguration.class, 
				SessionAutoConfiguration.class,
				RabbitAutoConfiguration.class,
				
				ActiveMQAutoConfiguration.class
		}
)
//@EnableJms
@Configuration
@Import({ActivemqTestContext.class, ActivemqConfiguration.class})
public class ArtemisProducerApplication {
	/*@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}*/

	public static void main(String[] args) {
		SpringApplication.run(ArtemisProducerApplication.class, args);
	}
	

	@Configuration
//	@PropertySource("classpath:activemq-test.properties")
	@ComponentScan
	@EnableDbm
	@EnableTransactionManagement
	public static class ActivemqTestContext {
		
		@Bean
		public DataSource dataSource(){
			JFishProperties jp = SpringUtils.loadAsJFishProperties("activemq-test.properties");
			DataSource ds = DataSourceBuilder.create()
											 .url(jp.getProperty("jdbc.url"))
											 .driverClassName(jp.getProperty("jdbc.driverClassName"))
											 .username(jp.getProperty("jdbc.username"))
											 .password(jp.getProperty("jdbc.password"))
											 .build();
			return ds;
		}
	}
	
	
}
