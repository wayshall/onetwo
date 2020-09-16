package org.onetwo.ext.rmqwithonsclient.producer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onetwo.boot.mq.entity.SendMessageEntity;
import org.onetwo.boot.mq.repository.SendMessageRepository;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.ds.DatasourceFactoryBean;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.DefaultDbmConfig;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.ext.ons.annotation.EnableONSClient;
import org.onetwo.ext.ons.annotation.ONSProducer;
import org.onetwo.ext.ons.producer.OnsDatabaseTransactionMessageInterceptor;
import org.onetwo.ext.rmqwithonsclient.producer.RmqONSProducerTest.ProducerTestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@ContextConfiguration(classes=ProducerTestContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Transactional
public class RmqONSProducerTest {
	public static final String TOPIC = "${topic}";
	public static final String PRODUER_ID = "${producerId}";
	public static final String ORDER_PAY = "${tags.orderPay}";
	public static final String ORDER_CANCEL = "${tags.orderCancel}";

	@Autowired
	DataBaseProducerServiceImpl dataBaseProducerService;
	@Autowired
	TestDatabaseTransactionMessageInterceptor testDatabaseTransactionMessageInterceptor;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Test
	public void test1SendMessage(){
		baseEntityManager.removeAll(SendMessageEntity.class);
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
		dataBaseProducerService.sendMessage();
		messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(1);
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	@Test
	public void test2sendMessageWithException(){
		baseEntityManager.removeAll(SendMessageEntity.class);
		try {
			dataBaseProducerService.sendMessageWithException();
			Assert.fail();
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ServiceException.class);
		}
		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(0);
//		LangUtils.CONSOLE.exitIf("test");
	}
	
	/***
	 * 测试事务成功后，发送消息失败，不影响消息发送，有补偿任务代发
	 * @author wayshall
	 */
	@Test
	public void test3sendMessageWithExceptionWhenExecuteSendMessage(){
		baseEntityManager.removeAll(SendMessageEntity.class);
		
		testDatabaseTransactionMessageInterceptor.setThrowWhenExecuteSendMessage(true);
//		LangUtils.await(3);
		dataBaseProducerService.sendMessage();

		int messageCount = baseEntityManager.countRecord(SendMessageEntity.class).intValue();
		assertThat(messageCount).isEqualTo(1);
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
		public DbmConfig dbmConfig(){
			DefaultDbmConfig dbmConfig = new DefaultDbmConfig();
			dbmConfig.setAutoProxySessionTransaction(true);
			return dbmConfig;
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
	
	public static class TestDatabaseTransactionMessageInterceptor extends OnsDatabaseTransactionMessageInterceptor {
		private volatile boolean throwWhenExecuteSendMessage;
		@Override
		@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
		public void afterCommit(SendMessageEvent event){
			if(throwWhenExecuteSendMessage){
				throw new ServiceException("send error afterCommit");
			}
			super.afterCommit(event);
		}
		public void setThrowWhenExecuteSendMessage(boolean throwWhenExecuteSendMessage) {
			this.throwWhenExecuteSendMessage = throwWhenExecuteSendMessage;
		}
	}
	
	@Data
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class OrderTestMessage {
		Long orderId;
		String title;
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OrderTestMessage other = (OrderTestMessage) obj;
			if (orderId == null) {
				if (other.orderId != null)
					return false;
			} else if (!orderId.equals(other.orderId))
				return false;
			return true;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
			return result;
		}
		
	}
}
