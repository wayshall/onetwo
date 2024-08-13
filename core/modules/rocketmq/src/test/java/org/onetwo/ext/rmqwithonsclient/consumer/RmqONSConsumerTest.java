package org.onetwo.ext.rmqwithonsclient.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.ons.consumer.TestConsumer;
import org.onetwo.ext.rmqwithonsclient.consumer.RmqONSConsumerTest.RmqConsumerTestContext;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=RmqConsumerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RmqONSConsumerTest {
	public static final String PRODUER_ID = "RmqONSConsumerTestProducer";

	@Test
	public void testReceiveMessage(){
		LangUtils.CONSOLE.exitIf("exit");
	}
	
	@EnableONSClient(producers=@ONSProducer(producerId=PRODUER_ID))
	@Configuration
	@PropertySource("classpath:rmqwithonsclient-test.properties")
	@ComponentScan
	@EnableDbm
	public static class RmqConsumerTestContext {
		@Bean
		public TestConsumer testConsumer(){
			return new TestConsumer();
		}

		@Bean
		public DatasourceFactoryBean dataSource(){
			DatasourceFactoryBean ds = new DatasourceFactoryBean();
			ds.setImplementClass(org.apache.tomcat.jdbc.pool.DataSource.class);
			ds.setPrefix("jdbc.");
			return ds;
		}
		
		@Bean
		public PropertyPlaceholderConfigurer jfishPropertyPlaceholder(){
			return SpringUtils.newApplicationConf("rmqwithonsclient-test.properties");
		}
		
		@Bean
		public DbmConfig dbmConfig(){
			DefaultDbmConfig dbmConfig = new DefaultDbmConfig();
			dbmConfig.setAutoProxySessionTransaction(true);
			return dbmConfig;
		}
	}
}
