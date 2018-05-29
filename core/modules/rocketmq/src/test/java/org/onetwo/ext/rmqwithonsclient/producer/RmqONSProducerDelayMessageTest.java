package org.onetwo.ext.rmqwithonsclient.producer;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerDelayMessageTest.ProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RmqONSProducerDelayMessageTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "${producerId}";
	public static final String ORDER_PAY = "${tags.orderPay}";
	public static final String ORDER_CANCEL = "${tags.orderCancel}";

	@Autowired
	DataBaseProducerServiceImpl dataBaseProducerService;
	
	@Test
	public void test4sendDelayMessage(){
		dataBaseProducerService.sendDelayMessage("2018-05-29 16:27:00");
		dataBaseProducerService.sendDelayMessage("2018-05-29 16:40:00");
		dataBaseProducerService.sendDelayMessage("2018-05-29 16:45:00");
		dataBaseProducerService.sendDelayMessage("2018-05-29 16:47:00");
		dataBaseProducerService.sendDelayMessage("2018-05-29 16:55:00");
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@EnableONSClient(producers=@ONSProducer(producerId=PRODUER_ID))
	@Configuration
	@PropertySource("classpath:rmqwithonsclient-test.properties")
	@EnableDbm
	@EnableTransactionManagement
	public static class ProducerTestContext {
		@Bean
		public DatasourceFactoryBean datasourceFactoryBean(){
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
		public DataBaseProducerServiceImpl dataBaseProducerService(){
			return new DataBaseProducerServiceImpl();
		}

	}
	
}
