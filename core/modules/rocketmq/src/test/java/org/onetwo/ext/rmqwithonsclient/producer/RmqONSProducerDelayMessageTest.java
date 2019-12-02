package org.onetwo.ext.rmqwithonsclient.producer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.ons.consumer.TestConsumer;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerDelayMessageTest.ProducerTestContext;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.OrderTestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.collect.Lists;

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
	public static CountDownLatch delayCountDownLatch = new CountDownLatch(3);
	public static List<OrderTestMessage> messages = Lists.newArrayList();
	
	@Test
	public void test4sendDelayMessage() throws Exception{
		Date now = new Date();
		OrderTestMessage msg = dataBaseProducerService.sendDelayMessage(DateUtils.addMinutes(now, -1));
		messages.add(msg);
		msg = dataBaseProducerService.sendDelayMessage(DateUtils.addSeconds(now, 30));
		messages.add(msg);
		msg = dataBaseProducerService.sendDelayMessage(DateUtils.addSeconds(now, 40));
		messages.add(msg);
		
		delayCountDownLatch.await();
		assertThat(messages).isEmpty();
		LangUtils.await(10);
//		LangUtils.CONSOLE.exitIf("exit");
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
		@Bean
		public TestConsumer testConsumer(){
			return new TestConsumer();
		}
	}
	
}
