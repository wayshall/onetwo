package org.onetwo.ext.rmqwithonsclient.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.boot.mq.SendMessageRepository;
import org.onetwo.boot.mq.SimpleDatabaseTransactionMessageInterceptor;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.ProducerTestContext;
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
public class RmqONSProducerTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "${producerId}";
	public static final String ORDER_PAY = "${tags.orderPay}";
	public static final String ORDER_CANCEL = "${tags.orderCancel}";

	@Autowired
	DataBaseProducerServiceImpl dataBaseProducerService;
	@Autowired
	TestDatabaseTransactionMessageInterceptor testDatabaseTransactionMessageInterceptor;
	
	@Test
	public void test1SendMessage(){
		dataBaseProducerService.sendMessage();
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@Test
	public void test2sendMessageWithException(){
		dataBaseProducerService.sendMessageWithException();
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@Test
	public void test3sendMessageWithExceptionWhenExecuteSendMessage(){
		testDatabaseTransactionMessageInterceptor.setThrowWhenExecuteSendMessage(true);
		LangUtils.await(3);
		dataBaseProducerService.sendMessage();
		LangUtils.CONSOLE.exitIf("test");
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
		public TestDatabaseTransactionMessageInterceptor databaseTransactionMessageInterceptor(SendMessageRepository sendMessageRepository){
			TestDatabaseTransactionMessageInterceptor interceptor = new TestDatabaseTransactionMessageInterceptor();
			interceptor.setSendMessageRepository(sendMessageRepository);
			return interceptor;
		}
	}
	
	public static class TestDatabaseTransactionMessageInterceptor extends SimpleDatabaseTransactionMessageInterceptor {
		private volatile boolean throwWhenExecuteSendMessage;
		@Override
		public void afterCommit(SendMessageEvent event){
			if(throwWhenExecuteSendMessage){
				throw new ServiceException("send error");
			}
			super.afterCommit(event);
		}
		public void setThrowWhenExecuteSendMessage(boolean throwWhenExecuteSendMessage) {
			this.throwWhenExecuteSendMessage = throwWhenExecuteSendMessage;
		}
	}
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderTestMessage {
		Long orderId;
		String title;
	}
}
